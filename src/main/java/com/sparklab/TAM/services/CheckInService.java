package com.sparklab.TAM.services;

import com.sparklab.TAM.dto.calendar.ApartmentCalendarDTO;
import com.sparklab.TAM.dto.calendar.CalendarResponseDTO;
import com.sparklab.TAM.dto.report.FilterDTO;
import com.sparklab.TAM.dto.reservation.ReservationDTO;
import com.sparklab.TAM.model.CheckIn;
import com.sparklab.TAM.repositories.CheckInRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final ReservationService reservationService;


    public ResponseEntity<?> doCheckIn(String userId, int apartmentId) {

        // Get date today
        LocalDateTime dateTimeToday = LocalDateTime.now();
        LocalDate dateToday = dateTimeToday.toLocalDate();

        // Format LocalDate to a string in "yyyy-MM-dd" format
        String formattedDateToday = dateToday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


        FilterDTO filterDTO = new FilterDTO();
        filterDTO.setApartment(apartmentId);
        filterDTO.setFromDate(dateToday);


        List<ReservationDTO> reservationsFiltered = reservationService.getReservationsFiltered(userId, filterDTO);

        Optional<ReservationDTO> reservationOptional = reservationsFiltered.stream()
                .filter(r -> r.getArrival().equals(formattedDateToday))
                .findFirst();

        if (reservationOptional.isEmpty()) {
            return new ResponseEntity<>("There is no reservation for this Apartment today!", HttpStatus.BAD_REQUEST);
        }

        ReservationDTO reservation = reservationOptional.get();

        String reservationDateString = reservation.getArrival();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate reservationDateFormatted = LocalDate.parse(reservationDateString, formatter);


        if (!dateToday.isEqual(reservationDateFormatted)) {
            return new ResponseEntity<>("You cant check in today", HttpStatus.TOO_EARLY);
        }

        LocalTime twoPM = LocalTime.of(14, 0);

        if (!dateTimeToday.isAfter(LocalDateTime.of(dateToday, twoPM))) {
            return new ResponseEntity<>("It is to early to check in, please try again after 2PM", HttpStatus.TOO_EARLY);
        }

        if (checkInRepository.existsByReservationId(String.valueOf(reservation.getId()))) {
            return new ResponseEntity<>("You have completed the check-in", HttpStatus.BAD_REQUEST);
        }

        CheckIn checkIn = new CheckIn();
        checkIn.setFirstName(reservation.getFirstname());
        checkIn.setLastName(reservation.getLastname());
        checkIn.setReservationId(String.valueOf(reservation.getId()));

        checkIn.setTimeOfCheckIn(LocalDateTime.now());

        LocalDateTime checkoutDate = LocalDateTime.of(LocalDate.parse(reservation.getDeparture(),formatter), LocalTime.MIDNIGHT);

        checkIn.setTimeOfCheckOut(checkoutDate);
        checkInRepository.save(checkIn);

        return new ResponseEntity<>("Check-In COMPLETE!", HttpStatus.OK);

    }


    @Scheduled(cron = "0 0 14 * * *") // Scheduled to run every day at 2 PM
    public void doCheckOut() {

        LocalDate dateToday = LocalDate.now();

        String dateTodayFormatted = dateToday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<ApartmentCalendarDTO> reservations = reservationService.getAllReservationsByDateCalendar("3", dateTodayFormatted, dateTodayFormatted);

        List<CalendarResponseDTO> todayCheckout = new ArrayList<>();

        for (ApartmentCalendarDTO reservation : reservations) {
            List<CalendarResponseDTO> validReservations = reservation.getReservations().stream().filter(r -> r.getDeparture().equals(dateTodayFormatted)).collect(Collectors.toList());
            todayCheckout.addAll(validReservations);
        }

        if (todayCheckout.size() != 0) {
            List<Integer> reservationIds = todayCheckout.stream().map(CalendarResponseDTO::getId).toList();

            for (Integer resId : reservationIds) {

                CheckIn checkOut = checkInRepository.findByReservationId(String.valueOf(resId));
                checkOut.setCheckOut(true);
                checkOut.setTimeOfCheckOut(LocalDateTime.now());

                checkInRepository.save(checkOut);
            }
        }


    }

}
