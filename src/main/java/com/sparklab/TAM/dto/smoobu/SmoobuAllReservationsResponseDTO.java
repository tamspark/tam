package com.sparklab.TAM.dto.smoobu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sparklab.TAM.dto.reservation.ReservationDTO;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmoobuAllReservationsResponseDTO {
  private int page_count;
  private int page_size;
  private int total_items;
  private int page;
  private List<ReservationDTO> bookings;

}
