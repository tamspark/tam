package com.sparklab.TAM.dto.registerandlogin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfirmationRequest {
    private String password;
    private LocalDateTime confirmationDate = LocalDateTime.now();
}
