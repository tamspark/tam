package com.sparklab.TAM.dto.OpenAI.Assistant;

import lombok.Data;

@Data
public class RunAssistantRequest {

    String assistant_id;
    String instructions;

}
