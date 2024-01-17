package com.sparklab.TAM.dto.calendar;


import lombok.Data;

import java.time.LocalDate;
@Data
public class CalendarRequestDTO {

private LocalDate fromDate;
private LocalDate toDate;

}
