package com.sparklab.TAM.dto.IOTAuth;

import lombok.Data;

@Data
public class IOTAccountDTO {

    private Long id;
    private int apartmentId;
    private String secretKey;
    private String apiKey;
    private Long userId;

}
