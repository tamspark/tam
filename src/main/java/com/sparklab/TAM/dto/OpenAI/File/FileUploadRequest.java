package com.sparklab.TAM.dto.OpenAI.File;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
public class FileUploadRequest {
    private String purpose;
    private MultipartFile file;
}
