package com.sparklab.TAM.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "conversations")
public class Conversation extends BaseEntity {

    private int reservationId;
    private String guestName;
    private String apartmentName;
    private long lastChecked;

}
