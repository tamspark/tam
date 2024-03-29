package com.sparklab.TAM.dto.whatsappApi.sendMessageDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessageTemplateDTO {

    @JsonProperty("messaging_product")
    private String messagingProduct;
    private String to;
    private String type;
    private Template template;
}
