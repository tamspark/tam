package com.sparklab.TAM.dto.OpenAI.Assistant;

import lombok.Data;

import java.util.List;

@Data
public class AssistantCreationRequest {
    private String name;
    private String instructions;
    private String model;
    private List<Tool> tools;
    private List<String> file_ids;
}
