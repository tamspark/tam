package com.sparklab.TAM.dto.OpenAI.Assistant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RunAssistantResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("object")
    private String object;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private long createdAt;

    @JsonProperty("assistant_id")
    private String assistantId;

    @JsonProperty("thread_id")
    private String threadId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("started_at")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private long startedAt;

    @JsonProperty("expires_at")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Long expiresAt;

    @JsonProperty("cancelled_at")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Long cancelledAt;

    @JsonProperty("failed_at")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Long failedAt;

    @JsonProperty("completed_at")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private long completedAt;

    @JsonProperty("last_error")
    private String lastError;

    @JsonProperty("model")
    private String model;

    @JsonProperty("instructions")
    private String instructions;

    @JsonProperty("tools")
    private List<Tool> tools;

    @JsonProperty("file_ids")
    private List<String> fileIds;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

}
