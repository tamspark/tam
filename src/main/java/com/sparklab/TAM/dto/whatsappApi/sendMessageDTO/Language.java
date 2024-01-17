package com.sparklab.TAM.dto.whatsappApi.sendMessageDTO;

import lombok.Data;

@Data
public class Language {
    private String code;

    public Language(String code) {
        this.code = code;
    }
}

