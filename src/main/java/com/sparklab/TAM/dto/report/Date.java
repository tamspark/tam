package com.sparklab.TAM.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Date {
    private Double price;
    private Integer min_length_of_stay;
    private Integer available;

}
