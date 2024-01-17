package com.sparklab.TAM.dto.rate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sparklab.TAM.dto.reservation.Operation;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RatesDTO {
    private List<Integer> apartments;
    private List<Operation> operations;

}

