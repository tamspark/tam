package com.sparklab.TAM.services;

import com.sparklab.TAM.dto.report.FilterDTO;
import com.sparklab.TAM.dto.reservation.ReservationDTO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilterService {


    public List<ReservationDTO> getFilteredReservations(List<ReservationDTO> reservations, FilterDTO filterDTO) {

        List<ReservationDTO> filteredreservations = reservations.stream().filter(reservation -> matchesFilterCriteria(reservation, filterDTO)).collect(Collectors.toList());
        return filteredreservations;
    }

    public boolean matchesFilterCriteria(ReservationDTO reservation, FilterDTO filter) {

        Double price = reservation.getPrice();

        return ((filter.getApartment() == 0 || reservation.getApartment().getId() == filter.getApartment())
                && (filter.getAdults() == 0 || filter.getAdults() == reservation.getAdults())
                && (filter.getChildren() == 0 || filter.getChildren() == reservation.getChildren())
                && (filter.getGuestName() == null || reservation.getGuestName().contains(filter.getGuestName()))
                && (filter.getFirstname() == null || reservation.getFirstname().contains(filter.getFirstname()))
                && (filter.getLastname() == null || reservation.getLastname().contains(filter.getLastname()))
                && (filter.getEmail() == null || reservation.getEmail().contains(filter.getEmail()))
                && (filter.getChannel() == null || reservation.getChannel().getName().contains(filter.getChannel()))
                && (filter.getMinprice() == 0 || filter.getMaxprice() == 0 || (price >= filter.getMinprice() && price <= filter.getMaxprice()))
        );
    }
}

