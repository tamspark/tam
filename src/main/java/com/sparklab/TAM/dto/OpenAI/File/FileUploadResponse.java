package com.sparklab.TAM.dto.OpenAI.File;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Instant;

@Data
public class FileUploadResponse {

    private String id;
    private String object;
    private long bytes;
    private Instant created_at;
    private String filename;
    private String purpose;
    private String status;
    private String status_details;

}
