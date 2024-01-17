package com.sparklab.TAM.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sparklab.TAM.dto.report.Date;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetRatesDTO {

    private Map<String, Map<String, Date>> data;

    public void filterDateByPrice(Map<String, Map<String, Date>> ratesData) {
        ratesData.forEach((apartmentId, dateMap) -> {
            dateMap.entrySet().removeIf(entry -> entry.getValue().getPrice() == null);
        });
        this.data = ratesData;
    }


}
