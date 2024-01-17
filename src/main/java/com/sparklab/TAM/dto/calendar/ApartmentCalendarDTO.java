package com.sparklab.TAM.dto.calendar;

import lombok.Data;

import java.util.List;

@Data
public class ApartmentCalendarDTO {

    private int apartmentId;
    private String apartmentName;
    private List<CalendarResponseDTO> reservations;


    public ApartmentCalendarDTO (int apartmentId, String name,List<CalendarResponseDTO> reservations) {
        this.apartmentId = apartmentId;
        this.apartmentName=name;
        this.reservations = reservations;
    }
}
