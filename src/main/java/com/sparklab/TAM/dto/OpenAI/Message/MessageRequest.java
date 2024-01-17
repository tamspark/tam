package com.sparklab.TAM.dto.OpenAI.Message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class MessageRequest {
    String role;
    String content;

    @JsonProperty("file_ids")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<String> fileIds;
}
