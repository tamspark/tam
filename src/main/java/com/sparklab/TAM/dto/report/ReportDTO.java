package com.sparklab.TAM.dto.report;

import com.sparklab.TAM.dto.report.FilterDTO;
import lombok.Data;

import java.util.List;

@Data
public class ReportDTO {

    private double avgNrOfGuests;
    private double averagePrice;
    private double avgLengthOfStay;
    private int reservationsNumber;
    private String channelWithMostBooking;
    private List<String> topApartmentsName;
    private FilterDTO filterDTO;
}
