package com.sparklab.TAM.dto.report;

import lombok.Data;
import java.time.LocalDate;

@Data
public class FilterDTO {
    private int apartment;
    private String channel;
    private String guestName;
    private String firstname;
    private String lastname;
    private String email;
    private int adults;
    private int children;
    private LocalDate fromDate;
    private LocalDate toDate;
    private double minprice;
    private double maxprice;
}
