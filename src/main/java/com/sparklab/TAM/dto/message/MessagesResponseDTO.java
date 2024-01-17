package com.sparklab.TAM.dto.message;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessagesResponseDTO {
    private int page_count;
    private int page_size;
    private int total_items;
    private int page;
    private List<MessageDTO> messages;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class MessageDTO {
        private int id;
        private String subject;
        private String message;
        private String htmlMessage;
        private int type;
    }
}


