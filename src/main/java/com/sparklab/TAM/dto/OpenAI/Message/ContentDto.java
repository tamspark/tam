package com.sparklab.TAM.dto.OpenAI.Message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ContentDto {

    @JsonProperty("type")
    private String type;

    @JsonProperty("text")
    private TextDto text;
}
