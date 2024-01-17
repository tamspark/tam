package com.sparklab.TAM.dto.apartment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Apartment {
    private int id;
    private String name;
    private Location location;
    private String timeZone;
    private Rooms rooms;
    private List<String> amenities;
    private String currency;
    private Price price;
    private Type type;
}