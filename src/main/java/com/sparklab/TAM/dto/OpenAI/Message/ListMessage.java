package com.sparklab.TAM.dto.OpenAI.Message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ListMessage {

    @JsonProperty("object")
    String object;
    @JsonProperty("data")
    List<ThreadMessageDto> data;

    @JsonProperty("first_id")
    String firstId;

    @JsonProperty("last_id")
    String lastId;

    @JsonProperty("has_more")
    Boolean hasMore;

}
