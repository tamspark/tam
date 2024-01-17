package com.sparklab.TAM.dto.whatsappApi.responseDTO;

import lombok.Data;

import java.util.List;

@Data
public class WhatsAppMessageResponseDTO {
    private String messagingProduct;
    private List<Contact> contacts;
    private List<Message> messages;
}
