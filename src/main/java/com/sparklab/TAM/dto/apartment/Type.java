package com.sparklab.TAM.dto.apartment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Type {
    private int id;
    private String name;
}