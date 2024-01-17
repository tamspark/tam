package com.sparklab.TAM.contollers;

import com.sparklab.TAM.dto.OpenAI.Assistant.AssistantCreationResponse;
import com.sparklab.TAM.dto.OpenAI.File.FileUploadResponse;
import com.sparklab.TAM.dto.OpenAI.Message.ListMessage;
import com.sparklab.TAM.dto.OpenAI.Message.MessageRequest;
import com.sparklab.TAM.dto.OpenAI.Thread.ThreadDtoResponse;
import com.sparklab.TAM.model.OpenAIAssistant;
import com.sparklab.TAM.model.ThreadHistory;
import com.sparklab.TAM.repositories.OpenAIAssistantRepository;
import com.sparklab.TAM.repositories.ThreadHistoryRepository;
import com.sparklab.TAM.services.AvailabilityFileService;
import com.sparklab.TAM.services.OpenAIService;
import com.sparklab.TAM.services.ReservationFileService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("TAM/assistant")
public class AssistantController {

    private final OpenAIService openAIService;
    private final OpenAIAssistantRepository aiAssistantRepository;
    private final ThreadHistoryRepository threadHistoryRepository;

    private final ReservationFileService reservationFileService;


    @PostMapping("/create/{type}")
    public AssistantCreationResponse createAssistant(@RequestPart("file") MultipartFile file,
                                                     @PathVariable String type) {
        return openAIService.createAssistant(file, type);
    }

    @PostMapping("/file")
    public FileUploadResponse sendFile(@RequestPart("file") MultipartFile file) {
        return openAIService.sendFile(file);
    }

    @GetMapping()
    public List<OpenAIAssistant> findAll() {
        return aiAssistantRepository.findAll();
    }


    @PostMapping("/thread/{type}")
    public ThreadDtoResponse createThread(@RequestBody String message,
                                          @PathVariable String type,
                                          @RequestParam(required = false) Integer apartmentId) {
        return openAIService.createThread(message, type, apartmentId);
    }


    @GetMapping("/thread/{threadId}/message")
    public ListMessage getAllMessages(@PathVariable String threadId) {
        return openAIService.getAllMessages(threadId);
    }


    @PostMapping("/chat/{threadId}/{type}")
    public MessageRequest chat(@PathVariable String threadId,
                               @RequestBody MessageRequest messageRequest,
                               @PathVariable String type) {
        messageRequest.setRole("assistant");
        return openAIService.chat(threadId, messageRequest, type);
    }

    @GetMapping("/thread/getAll")
    public List<ThreadHistory> findALl() {
        return threadHistoryRepository.findAll();
    }

    @GetMapping("TAM/hostFile")
    public void createHostFile(){
        reservationFileService.writeApartmentsToFile();
    }

}
