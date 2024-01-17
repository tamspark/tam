package com.sparklab.TAM.dto.OpenAI.Message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TextDto {

    @JsonProperty("value")
    private String value;

    @JsonProperty("annotations")
    private List<Object> annotations;
}
