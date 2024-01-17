package com.sparklab.TAM.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PhotoDTO {
    Long id;
    int apartmentId;

    String downloadURL;
    String fileName;
    String fileType;
    Long fileSize;


    byte[] fileByteArray;

    MultipartFile file;

}
