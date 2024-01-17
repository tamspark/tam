package com.sparklab.TAM.dto.Dashboard;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sparklab.TAM.utils.DecimalJsonSerializer;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class OccupancyRevenueReportDTO {

    String month;

    Map<String, Double> data;

    public void setData(Double revenue,Double occupancy) {
        Map<String, Double> data = new HashMap<>();
        data.put("revenue", revenue);
        data.put("occupancy", occupancy);

        this.data = data;
    }


}
