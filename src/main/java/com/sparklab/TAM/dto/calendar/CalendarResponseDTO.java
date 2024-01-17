package com.sparklab.TAM.dto.calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sparklab.TAM.dto.smoobu.ChannelDTO;
import com.sparklab.TAM.dto.smoobu.SmoobuShortApartmentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarResponseDTO {
    private int id;
    private String arrival;
    private String departure;
    private String modifiedAt;
    private SmoobuShortApartmentDTO apartment;
    private ChannelDTO channel;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private int adults;
    private int children;
    private double price;
    private String createdAt;
    private String guestName;
    private String checkIn;
    private String checkOut;
    private List<String> allBookedDates;
    private String pricePaid;
    private boolean blocked_booking;



}
