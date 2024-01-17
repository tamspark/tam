package com.sparklab.TAM.dto.apartment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rooms {
    private int maxOccupancy;
    private int bedrooms;
    private int bathrooms;
    private int doubleBeds;
    private int singleBeds;
    private int sofaBeds;
    private int couches;
    private int childBeds;
    private int queenSizeBeds;
    private int kingSizeBeds;
}