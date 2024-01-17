package com.sparklab.TAM.dto.Dashboard;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sparklab.TAM.utils.DecimalJsonSerializer;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DashboardDTO {

    Map<String,Double> nightsPortalReport;

    @JsonSerialize(using = DecimalJsonSerializer.class)
    Double occupancyPercentage;

    List<OccupancyRevenueReportDTO> occupancyRevenueReport;

}
