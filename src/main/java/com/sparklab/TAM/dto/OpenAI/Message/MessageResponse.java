package com.sparklab.TAM.dto.OpenAI.Message;

import lombok.Data;

@Data
public class MessageResponse {
    String role;
    String message;
    Long createdTime;
}
