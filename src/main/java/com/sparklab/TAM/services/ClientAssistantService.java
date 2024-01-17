package com.sparklab.TAM.services;

import com.sparklab.TAM.dto.OpenAI.Assistant.AssistantCreationRequest;
import com.sparklab.TAM.dto.OpenAI.Assistant.RunAssistantRequest;
import com.sparklab.TAM.dto.OpenAI.Assistant.Tool;
import com.sparklab.TAM.dto.OpenAI.Message.MessageRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ClientAssistantService {
    //TODO To be automated
    private static final String ASSISTANT_ID = "asst_GK8PMYurKbz7dZtI2nKnHlU1";
    private final AvailabilityFileService availabilityFileService;


    public AssistantCreationRequest createClientAssistant(String fileId) {

        String instructionsWord = "You are an assistant that provides information about different properties based on the uploaded Word document. The price per night is " +
                "a reference price and if they ask about specific dates and availability you should send them this phone number to contact: +3551111111";

        AssistantCreationRequest request = new AssistantCreationRequest();
        request.setName("Data analyzer");
        request.setInstructions(instructionsWord);
        request.setModel("gpt-4-1106-preview");

        Tool tool = new Tool();
        tool.setType("retrieval");
        request.setTools(List.of(tool));

        request.setFile_ids(List.of(fileId));

        return request;
    }


    public Map<String, Object> createClientThread(String message, Integer apartmentId) {

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setRole("user");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDate.now().format(formatter);

        message = "create thread, date now: " + formattedDate + ". Give me information only for apartment with id: " + apartmentId;



        messageRequest.setContent(message);

        String fileId = availabilityFileService.createFile(apartmentId);

        messageRequest.setFileIds(List.of(fileId));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", List.of(messageRequest));

        System.out.println(requestBody);

        return requestBody;
    }

    public RunAssistantRequest runClientAssistantRequest() {
        RunAssistantRequest runAssistantRequest = new RunAssistantRequest();
        runAssistantRequest.setAssistant_id(ASSISTANT_ID);
        runAssistantRequest.setInstructions("Address user as Redis. Today's date CET is: " + LocalDate.now() + ". Read all the document for every response and every question" +
                "is related to the data of the document. Dont tell on your answer that you are pulling data from a document also dont ask if you should look for the" +
                " answer in the document, you should take it for granted. Structure the answer beautifully for example have new lines when needed or have " +
                "bullet-points presented the right way. You have two documents the docx document has the info about features of the home, the .txt document has " +
                "the data about availability for the next 2 months for each document. If you are asked something and you dont have the data or the answer advise" +
                "the user to call this number: +355681111111");

        return runAssistantRequest;

    }
}
