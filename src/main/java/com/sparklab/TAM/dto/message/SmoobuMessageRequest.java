package com.sparklab.TAM.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmoobuMessageRequest {
    private String subject;
    private String messageBody;

}
