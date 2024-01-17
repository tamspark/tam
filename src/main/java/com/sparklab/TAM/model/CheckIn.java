package com.sparklab.TAM.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "check_in")
public class CheckIn extends BaseEntity{
    private String reservationId;
    private String firstName;
    private String LastName;
    private boolean checkOut;
    private LocalDateTime timeOfCheckIn;
    private LocalDateTime timeOfCheckOut;

}
