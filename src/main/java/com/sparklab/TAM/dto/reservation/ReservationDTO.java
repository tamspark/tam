package com.sparklab.TAM.dto.reservation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparklab.TAM.dto.smoobu.ChannelDTO;
import com.sparklab.TAM.dto.smoobu.SmoobuShortApartmentDTO;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationDTO {
   private int id;
    @JsonProperty("reference-id")
    private int referenceId;
    private String type;
    private String arrival;
    private String departure;
    @JsonProperty("created-at")
    private String createdAt;
    private String modifiedAt;
    private SmoobuShortApartmentDTO apartment;
    private ChannelDTO channel;
   @JsonProperty("guest-name")
   private String guestName;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private int adults;
    private int children;
    @JsonProperty("check-in")
    private String checkIn;
    @JsonProperty("check-out")
    private String checkOut;
    private String notice;
    @JsonProperty("assistant-notice")
    private String assistantNotice;
    private double price;
    @JsonProperty("price-paid")
    private String pricePaid;
    @JsonProperty("commission-included")
    private String commissionIncluded;
    private int prepayment;
    @JsonProperty("prepayment-paid")
    private String prepaymentPaid;
    private int deposit;
    @JsonProperty("deposit-paid")
    private String depositPaid;
    private String language;
    @JsonProperty("guest-app-url")
    private String guestAppUrl;
    @JsonProperty("is-blocked-booking")
    private boolean blockedBooking;
    private int guestId;
    private List<SmoobuShortApartmentDTO> related;

}
