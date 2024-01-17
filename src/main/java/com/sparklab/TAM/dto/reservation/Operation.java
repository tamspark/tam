package com.sparklab.TAM.dto.reservation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Operation {
    List<String> dates;
    double daily_price;
    int min_length_of_stay;
}
