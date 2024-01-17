package com.sparklab.TAM.services;

import com.sparklab.TAM.converters.AssistantCreationResponseToOpenAIAssistant;
import com.sparklab.TAM.dto.OpenAI.Assistant.AssistantCreationRequest;
import com.sparklab.TAM.dto.OpenAI.Assistant.AssistantCreationResponse;
import com.sparklab.TAM.dto.OpenAI.Assistant.RunAssistantRequest;
import com.sparklab.TAM.dto.OpenAI.Assistant.RunAssistantResponse;
import com.sparklab.TAM.dto.OpenAI.File.FileUploadResponse;
import com.sparklab.TAM.dto.OpenAI.Message.ListMessage;
import com.sparklab.TAM.dto.OpenAI.Message.MessageRequest;
import com.sparklab.TAM.dto.OpenAI.Message.ThreadMessageDto;
import com.sparklab.TAM.dto.OpenAI.Thread.ThreadDtoResponse;
import com.sparklab.TAM.model.MessageHistory;
import com.sparklab.TAM.model.ThreadHistory;
import com.sparklab.TAM.repositories.MessageHistoryRepository;
import com.sparklab.TAM.repositories.OpenAIAssistantRepository;
import com.sparklab.TAM.repositories.ThreadHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class OpenAIService {


    private final OpenAIAssistantRepository openAIAssistantRepository;
    private final AssistantCreationResponseToOpenAIAssistant toOpenAIAssistant;
    private final MessageHistoryRepository messageHistoryRepository;
    private final ThreadHistoryRepository threadHistoryRepository;
    private final ApiCallService apiCallService;
    private final ClientAssistantService clientAssistantService;
    private final HostAssistantService hostAssistantService;


    //Step 1: Create A file for the assistant to use
    public FileUploadResponse sendFile(MultipartFile multipartFile) {
        //Refactored
        String apiUrl = "https://api.openai.com/v1/files";

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", multipartFile.getResource());
        builder.part("purpose", "assistants");

        FileUploadResponse response = apiCallService.postCallMultipartOpenAI(apiUrl, FileUploadResponse.class, builder);

        return response;
    }


    //Step 2: Create the assistant that will take the file/files and instructions
    public AssistantCreationResponse createAssistant(MultipartFile file, String type) {
        String apiUrl = "https://api.openai.com/v1/assistants";
        String fileId = sendFile(file).getId();

        AssistantCreationRequest request = new AssistantCreationRequest();

        if (type.equals("client")){
            request = clientAssistantService.createClientAssistant(fileId);
        }
        if (type.equals("host")){
            request = hostAssistantService.createHostAssistant(fileId);
        }

        AssistantCreationResponse response = apiCallService.postCallBodyOpenAI(apiUrl, AssistantCreationResponse.class, request);
        //Save assistant in DB
        openAIAssistantRepository.save(toOpenAIAssistant.convert(response));

        return response;
    }

    //Step 3: Create a thread
    public ThreadDtoResponse createThread(String message, String type,Integer apartmentId) {
        //Refactored
        String apiUrl = "https://api.openai.com/v1/threads";
        Map<String, Object> requestBody = new HashMap<>();

        if (type.equals("client")){
            requestBody = clientAssistantService.createClientThread(message,apartmentId);
        }
        if (type.equals("host")){
            requestBody = hostAssistantService.createHostThread(message);
        }

        ThreadDtoResponse threadResponse = apiCallService.postCallBodyOpenAI(apiUrl, ThreadDtoResponse.class, requestBody);

        ThreadHistory savedThreadHistory = threadHistoryRepository.save(new ThreadHistory(threadResponse.getId()));

        saveMessage(savedThreadHistory.getId(), "init thread", "first message");

        return threadResponse;

    }

    //Step 4: Send messages to that thread and initialize the assistant to respond to that message
    public MessageRequest chat(String threadId, MessageRequest messageRequest, String type) {
        //Refactored
        addMessage(threadId, messageRequest.getContent());
        String status;
        String runId = runAssistant(threadId,type).getId();
        do {

            status = checkStatus(threadId, runId);
            System.out.println(status);
            if (status.equals("expired"))
                return null;

        } while (!status.equals("completed"));


        ListMessage allMessages = getAllMessages(threadId);
        ThreadMessageDto lastMessage = allMessages.getData().get(0);


        String assistantResponse = lastMessage
                .getContent()
                .get(0)
                .getText()
                .getValue();

        ThreadHistory threadHistory = threadHistoryRepository.findThreadHistoryByThreadId(threadId);

        saveMessage(threadHistory.getId(), messageRequest.getContent(), assistantResponse);


        MessageRequest response = new MessageRequest();
        response.setContent(assistantResponse);
        response.setRole(lastMessage.getRole());

        return response;
    }

    public ListMessage getAllMessages(String threadId) {
        //Refactored
        String apiUrl = "https://api.openai.com/v1/threads/" + threadId + "/messages";
        ListMessage listMessage = apiCallService.getCallOpenAI(apiUrl, ListMessage.class);

        return listMessage;
    }


    private void addMessage(String threadId, String message) {
        //Refactored
        String apiUrl = "https://api.openai.com/v1/threads/" + threadId + "/messages";

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setRole("user");
        messageRequest.setContent(message);
        messageRequest.setFileIds(null);

        apiCallService.postCallBodyOpenAI(apiUrl, ThreadMessageDto.class, messageRequest);

    }

    private RunAssistantResponse runAssistant(String threadId,String type) {
        //Refactored
        String apiUrl = "https://api.openai.com/v1/threads/" + threadId + "/runs";

        RunAssistantRequest runAssistantRequest = new RunAssistantRequest();

        System.out.println(runAssistantRequest);

        if (type.equals("client")){
            runAssistantRequest = clientAssistantService.runClientAssistantRequest();
        }
        if (type.equals("host")){
            runAssistantRequest = hostAssistantService.runHostAssistantRequest();
        }

        RunAssistantResponse runAssistantResponse = apiCallService.postCallBodyOpenAI(apiUrl, RunAssistantResponse.class, runAssistantRequest);

        return runAssistantResponse;
    }

    private String checkStatus(String threadId, String runId) {
        //Refactored
        String status = "";
        String apiUrl = "https://api.openai.com/v1/threads/" + threadId + "/runs/" + runId;

        try {
            RunAssistantResponse runAssistantResponse = apiCallService.getCallOpenAI(apiUrl, RunAssistantResponse.class);

            status = runAssistantResponse.getStatus();
            Thread.sleep(500);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return status;
    }


    private void saveMessage(Long threadId, String requestMessage, String responseMessage) {
        //Refactored
        MessageHistory messageHistory = new MessageHistory();
        messageHistory.setRequestMessage(requestMessage);
        messageHistory.setResponseMessage(responseMessage);
        messageHistory.setTimestamp(Instant.now().toEpochMilli());

        ThreadHistory threadHistory = new ThreadHistory();
        threadHistory.setId(threadId);

        messageHistory.setThreadHistory(threadHistory);

        messageHistoryRepository.save(messageHistory);
    }

}
