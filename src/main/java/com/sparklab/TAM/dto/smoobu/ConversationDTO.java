package com.sparklab.TAM.dto.smoobu;

import lombok.Data;

@Data
public class ConversationDTO {
    Long id;
    int reservationId;
    String guestName;
    String apartmentName;
    long lastChecked;
}
