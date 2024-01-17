package com.sparklab.TAM.dto.OpenAI.UploadJSON;

import lombok.Data;

@Data
public class ApartmentDataSimple {
    String name;
    Double price;
    int minLengthOfStay;
    int available;
}
