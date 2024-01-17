package com.sparklab.TAM.dto.OpenAI.Assistant;

import lombok.Data;

import java.util.List;


@Data
public class AssistantCreationResponse {
    private String id;
    private String object;
    private String name;
    private String description;
    private String model;
    private String created_at;
    private String instructions;
    private List<Tool> tools;
    private List<String> file_ids;
    private Object metadata;
}

