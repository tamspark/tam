package com.sparklab.TAM.services;

import com.sparklab.TAM.dto.smoobu.ChannelDTO;
import com.sparklab.TAM.dto.report.FilterDTO;
import com.sparklab.TAM.dto.report.ReportDTO;
import com.sparklab.TAM.dto.reservation.ReservationDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {


    public double getAvgPriceBetweenTwoDates(List<ReservationDTO> reservations) {
        double avgPrice = reservations.stream().mapToDouble(ReservationDTO::getPrice).average().getAsDouble();
        return avgPrice;

    }

    public double getAvgNumberOrfGuestsBetweenTwoDates(List<ReservationDTO> reservations) {
        double avgNrOfGuests = reservations.stream().mapToDouble(reservation -> reservation.getAdults() + reservation.getChildren()).average().orElse(0.0);
        return avgNrOfGuests;
    }

    public double getAvgLengthOfStay(List<ReservationDTO> reservations) {

        long totalLengthOfStay = 0;
        long numberOfReservations = 0;
        for (ReservationDTO reservation : reservations) {

            LocalDate arrivalDate = LocalDate.parse(reservation.getArrival());
            LocalDate departureDate = LocalDate.parse(reservation.getDeparture());

            long lengthOfStay = ChronoUnit.DAYS.between(arrivalDate, departureDate);
            totalLengthOfStay += lengthOfStay;
            numberOfReservations++;

        }
        if (numberOfReservations > 0) {
            double avgLengthOfStay = (double) totalLengthOfStay / numberOfReservations;
            return avgLengthOfStay;
        } else {
            return 0.0;
        }
    }

    public int getNumberOfReservationsBetweenTwoDates(List<ReservationDTO> reservations) {
        return reservations.size();
    }


    public List<String> topAparmentsBookedBetweenTwoDates(List<ReservationDTO> reservations) {
        Map<String, Long> apartmentBookings = reservations.stream().collect(Collectors.groupingBy(reservation -> reservation.getApartment().getName(), Collectors.counting()));
        List<String> topApartmentNames = apartmentBookings.entrySet().stream().sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())).limit(5).map(Map.Entry::getKey).collect(Collectors.toList());
        return topApartmentNames;
    }


    public String findChannelWithMostBookings(List<ReservationDTO> reservations) {

        Map<ChannelDTO, Integer> channelBookingsCount = new HashMap<>();

        for (ReservationDTO reservation : reservations) {
            ChannelDTO channel = reservation.getChannel();

            channelBookingsCount.put(channel, channelBookingsCount.getOrDefault(channel, 0) + 1);
        }

        ChannelDTO channelWithMostBookings = null;
        int maxBookings = 0;

        for (Map.Entry<ChannelDTO, Integer> entry : channelBookingsCount.entrySet()) {
            if (entry.getValue() > maxBookings) {
                maxBookings = entry.getValue();
                channelWithMostBookings = entry.getKey();
            }
        }

        if (channelWithMostBookings != null) {
            return channelWithMostBookings.getName();
        } else {
            return "No bookings available";
        }
    }

    public ReportDTO generateReport(List<ReservationDTO> reservations, FilterDTO filterDTO) {
        ReportDTO report = new ReportDTO();
        report.setAvgNrOfGuests(getAvgNumberOrfGuestsBetweenTwoDates(reservations));
        report.setAveragePrice(getAvgPriceBetweenTwoDates(reservations));
        report.setAvgLengthOfStay(getAvgLengthOfStay(reservations));
        report.setReservationsNumber(getNumberOfReservationsBetweenTwoDates(reservations));
        report.setTopApartmentsName(topAparmentsBookedBetweenTwoDates(reservations));
        report.setChannelWithMostBooking(findChannelWithMostBookings(reservations));
        report.setFilterDTO(filterDTO);
        return report;

    }


}
