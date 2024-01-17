package com.sparklab.TAM.dto.photoWebScraping;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageInfoDTO {

    String base64Image;
    String fileType;
    String filename;


}
