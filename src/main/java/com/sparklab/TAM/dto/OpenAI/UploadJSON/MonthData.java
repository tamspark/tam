package com.sparklab.TAM.dto.OpenAI.UploadJSON;

import lombok.Data;

import java.util.List;

@Data
public class MonthData {


    String pricePerNight;
    String month;
    String year;

    List<String> availableDates;
    List<String> bookedDates;

}
