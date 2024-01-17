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
public class HostAssistantService {
    //TODO To be automated
    private static final String ASSISTANT_ID = "asst_SBLpZ6jAit0TQYYap6QJoq9M";
    private final ReservationFileService reservationFileService;

    public AssistantCreationRequest createHostAssistant(String fileId) {

        String instructionsHostAssistant = "You are an assistant that provides information about different properties based on the uploaded text document. Your purpose is to help the host of the " +
                "properties get information about guests arrival, check-in check out, how may days will they be staying who did the booking, how many people and so on. ";

        AssistantCreationRequest request = new AssistantCreationRequest();
        request.setName("Host data analyzer");

        request.setInstructions(instructionsHostAssistant);
        request.setModel("gpt-4-1106-preview");

        Tool tool = new Tool();
        tool.setType("retrieval");
        request.setTools(List.of(tool));

        request.setFile_ids(List.of(fileId));

        return request;
    }


    public Map<String, Object> createHostThread(String message) {

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setRole("user");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDate.now().format(formatter);

        message = "create thread, date now: " + formattedDate + ", User: Redis";
        messageRequest.setContent(message);


        String fileId = reservationFileService.writeApartmentsToFile();
        messageRequest.setFileIds(List.of(fileId));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", List.of(messageRequest));

        System.out.println(requestBody);

        return requestBody;
    }



    public RunAssistantRequest runHostAssistantRequest() {
        RunAssistantRequest runAssistantRequest = new RunAssistantRequest();
        runAssistantRequest.setAssistant_id(ASSISTANT_ID);
        runAssistantRequest.setInstructions("Address user as Redis. Today's date CET is: " + LocalDate.now() + ". Read all the document for every response and every question" +
                "is related to the data of the document. Dont tell on your answer that you are pulling data from a document also dont ask if you should look for the" +
                " answer in the document, you should take it for granted. Structure the answer beautifully for example have new lines when needed or have " +
                "bullet-points presented the right way. You have two documents the docx document has the info about features of the home, the .txt document has " +
                "the data about rooms/apartments ");

        return runAssistantRequest;

    }
}
