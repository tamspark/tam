package com.sparklab.TAM.dto.OpenAI.Thread;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ThreadDtoResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("object")
    private String object;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private long createdAt;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;
}
