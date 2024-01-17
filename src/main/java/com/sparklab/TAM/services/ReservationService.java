package com.sparklab.TAM.services;

import com.sparklab.TAM.configuration.SmoobuConfiguration;
import com.sparklab.TAM.converters.ReservationDTOToCalendarResponseDTO;
import com.sparklab.TAM.converters.ReservationDTOToProcessedReservation;
import com.sparklab.TAM.dto.calendar.ApartmentCalendarDTO;
import com.sparklab.TAM.dto.calendar.CalendarResponseDTO;
import com.sparklab.TAM.dto.report.FilterDTO;
import com.sparklab.TAM.dto.report.ReportDTO;
import com.sparklab.TAM.dto.reservation.ReservationDTO;
import com.sparklab.TAM.dto.smoobu.SmoobuAllReservationsResponseDTO;
import com.sparklab.TAM.exceptions.ApiCallError;
import com.sparklab.TAM.model.ProcessedReservations;
import com.sparklab.TAM.repositories.ProcessedReservationsRepository;
import com.sparklab.TAM.repositories.SmoobuAccountRepository;
import com.sun.jdi.InternalException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@EnableScheduling
public class ReservationService {

    private final ApiCallService apiCallService;
    private final ReservationDTOToCalendarResponseDTO reservationDTOToCalendarResponseDTO;
    private final ProcessedReservationsRepository processedReservationsRepository;
    private final SmoobuAccountRepository smoobuAccountRepository;
    private final ReservationDTOToProcessedReservation toProcessedReservation;
    private static final Logger logger = LogManager.getLogger(ChannelService.class);
    private final SmoobuConfiguration smoobuConfiguration;

    private final FilterService filterService;
    private final MessageService messageService;
    private ReportService reportService;

    public List<ApartmentCalendarDTO> getAllReservationsByDateCalendar(String userId, String fromDate, String toDate) {

        try {
            Long parseUserId = Long.parseLong(userId);
            String apiUrl = smoobuConfiguration.getApiURI() + "reservations?from=" + fromDate + "&to=" + toDate;
            List<CalendarResponseDTO> calendar= apiCallService.getMethod(apiUrl, SmoobuAllReservationsResponseDTO.class, parseUserId).getBookings().stream().map(reservationDTOToCalendarResponseDTO::convert).collect(Collectors.toList());
           return  getApartmentReservationCalendar(calendar);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InternalException("Response cannot be parsed as Reservation object");
        }
    }

    public ApartmentCalendarDTO getAllReservationByApartmentId(String userId, int apartmentId , String fromDate, String toDate){

        List<ApartmentCalendarDTO> apartmentCalendarDTOList = getAllReservationsByDateCalendar(userId,fromDate,toDate);

        Optional<ApartmentCalendarDTO> apartmentCalendarDTO = apartmentCalendarDTOList.stream().filter(a -> a.getApartmentId()==apartmentId).findFirst();

        return apartmentCalendarDTO.orElse(null);
    }

    private List<ApartmentCalendarDTO> getApartmentReservationCalendar(List<CalendarResponseDTO> calendar){
        Map<Integer, List<CalendarResponseDTO>> apartmentReservations = new HashMap<>();

        for (CalendarResponseDTO reservation : calendar) {
            int apartmentId = reservation.getApartment().getId();
            apartmentReservations.computeIfAbsent(apartmentId, k -> new ArrayList<>()).add(reservation);
            List<String> includedDates = calculateIncludedDates(reservation.getArrival(), reservation.getDeparture());
            reservation.setAllBookedDates(includedDates);
        }

        List<ApartmentCalendarDTO> result = new ArrayList<>();
        for (Map.Entry<Integer, List<CalendarResponseDTO>> entry : apartmentReservations.entrySet()) {
            int apartmentId = entry.getKey();
            List<CalendarResponseDTO> reservations = entry.getValue();
            String apartmentName = reservations.get(0).getApartment().getName();
            result.add(new ApartmentCalendarDTO(apartmentId, apartmentName, reservations));
        }

        return result;

    }

    private List<String> calculateIncludedDates(String arrival, String departure) {
        List<String> includedDates = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
        LocalDate startDate = LocalDate.parse(arrival, dateFormatter);
        LocalDate endDate = LocalDate.parse(departure, dateFormatter);
        while (!startDate.isAfter(endDate)) {
//            includedDates.add(String.valueOf(startDate.getDayOfMonth()));
            includedDates.add(startDate.format(dateFormatter));
            startDate = startDate.plusDays(1);
        }
        return includedDates;
    }


    public ReservationDTO getReservationById(String userId, String id) {
        try {
            Long parseUserId = Long.parseLong(userId);
            int parsedId = Integer.parseInt(id);
            String apiUrl = smoobuConfiguration.getApiURI() + "reservations/" + parsedId;
            // reservationDTOToCalendarResponseDTO.convert(apiCallService.getMethod(apiUrl, ReservationDTO.class));
            return apiCallService.getMethod(apiUrl, ReservationDTO.class, parseUserId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InternalException("Response cannot be parsed as Reservation object");
        }
    }

    public List<ReservationDTO> getReservationsFiltered(String userId, FilterDTO filterDTO) {
        try {
            Long parseUserId = Long.parseLong(userId);
            String apiUrl = smoobuConfiguration.getApiURI() + "reservations?from=" + filterDTO.getFromDate() + "&to=" + filterDTO.getToDate();
            return filterService.getFilteredReservations(apiCallService.getMethod(apiUrl, SmoobuAllReservationsResponseDTO.class, parseUserId).getBookings(), filterDTO);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InternalException("The reservations could not be filtered based on the specifications provided");
        }
    }

    public ReportDTO getReports(String userId, FilterDTO filterDTO) {
        try {
            Long parseUserId = Long.parseLong(userId);
            String apiUrl = smoobuConfiguration.getApiURI() + "reservations?from=" + filterDTO.getFromDate() + "&to=" + filterDTO.getToDate();
            List<ReservationDTO> reservations = filterService.getFilteredReservations(apiCallService.getMethod(apiUrl, SmoobuAllReservationsResponseDTO.class, parseUserId).getBookings(), filterDTO);
            return reportService.generateReport(reservations, filterDTO);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InternalException("The report could not be generated based on the specifications provided");
        }
    }

    @Scheduled(fixedRate = 1800000)  //1800000 = 30 min
    public void newReservationsLoop() {
//        USE THIS METHOD TO CALL OTHER METHODS EVERY TIME NEW RESERVATIONS ARE MADE

        List<Long> smoobuUserIds = smoobuAccountRepository.findAll()
                .stream()
                .map(smoobuAccount -> smoobuAccount.getUser().getId())
                .toList();

        for (Long userId : smoobuUserIds) {
            List<ReservationDTO> newReservations = getNewReservations(userId);

            if (!newReservations.isEmpty()) {
                processNewReservations(newReservations, userId);
            }
        }
    }

    private List<ReservationDTO> getNewReservations(Long userId) {
        String apiUrl = smoobuConfiguration.getApiURI() + "reservations?pageSize=100";


        List<ReservationDTO> newReservations;
        List<ReservationDTO> reservationsFromGetCall;

        List<Integer> oldReservationIds;

        try {
            oldReservationIds = processedReservationsRepository.findAll().stream().map(ProcessedReservations::getSmoobuId).toList();

            reservationsFromGetCall = apiCallService.getMethod(apiUrl, SmoobuAllReservationsResponseDTO.class, userId).getBookings();

            newReservations = !oldReservationIds.isEmpty() ? removeOldReservations(reservationsFromGetCall, oldReservationIds) : reservationsFromGetCall;

            return newReservations;
        } catch (ApiCallError e) {

            if (e.getErrorCode() == HttpStatusCode.valueOf(422)) {
                logger.error(e.getMessage(), e);
                throw new ApiCallError(e.getErrorCode(), "Response cannot be parsed as Apartment object");
            }
            logger.error(e.getMessage(), e);
            throw new InternalException();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }


    }

    private List<ReservationDTO> removeOldReservations(List<ReservationDTO> reservations, List<Integer> oldIds) {
        List<ReservationDTO> newReservations = new ArrayList<>();

        for (ReservationDTO reservation : reservations) {
            if (!oldIds.contains(reservation.getId())) {
                newReservations.add(reservation);
            }
        }
        return newReservations;
    }


    private void processNewReservations(List<ReservationDTO> reservations, Long userId) {
        List<ProcessedReservations> processedReservations = reservations.stream()
                .map(toProcessedReservation::convert)
                .collect(Collectors.toList());

        messageService.NewReservationWelcomeMessage(reservations, userId);
        processedReservationsRepository.saveAll(processedReservations);
    }

}
