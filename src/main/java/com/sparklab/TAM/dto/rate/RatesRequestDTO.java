package com.sparklab.TAM.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RatesRequestDTO {
    private LocalDate start_date;
    private LocalDate end_date;
   private List<Integer> apartments;
}
