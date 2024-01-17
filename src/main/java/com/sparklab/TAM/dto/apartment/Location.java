package com.sparklab.TAM.dto.apartment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    private String street;
    private String zip;
    private String city;
    private String state;
    private String country;
    private Double latitude;
    private Double longitude;
}