package com.sparklab.TAM.dto.OpenAI.Message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ThreadMessageDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("object")
    private String object;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private long createdAt;

    @JsonProperty("thread_id")
    private String threadId;

    @JsonProperty("role")
    private String role;

    @JsonProperty("content")
    private List<ContentDto> content;

    @JsonProperty("file_ids")
    private List<String> fileIds;

    @JsonProperty("assistant_id")
    private String assistantId;

    @JsonProperty("run_id")
    private String runId;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;
}
