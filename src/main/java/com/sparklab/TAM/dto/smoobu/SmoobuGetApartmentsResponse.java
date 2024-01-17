package com.sparklab.TAM.dto.smoobu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmoobuGetApartmentsResponse{
    List<SmoobuShortApartmentDTO> apartments;
}
