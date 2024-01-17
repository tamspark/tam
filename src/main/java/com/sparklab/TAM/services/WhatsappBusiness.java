package com.sparklab.TAM.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparklab.TAM.dto.whatsappApi.responseDTO.WhatsAppMessageResponseDTO;
import com.sparklab.TAM.dto.whatsappApi.sendMessageDTO.Language;
import com.sparklab.TAM.dto.whatsappApi.sendMessageDTO.MessageTemplateDTO;
import com.sparklab.TAM.dto.whatsappApi.sendMessageDTO.Template;
import com.sparklab.TAM.exceptions.ApiCallError;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@AllArgsConstructor
public class WhatsappBusiness {

    private final String API_KEY = "";
    private final WebClient webClient;
    private final ObjectMapper objectMapper;


    public WhatsAppMessageResponseDTO sendMessage(String toNumber, String templateName) {
        String version = "v17.0";
        String fromNumber = "145763698630318";
        String apiUrl = "https://graph.facebook.com/" + version + "/" + fromNumber + "/messages";

        MessageTemplateDTO message = new MessageTemplateDTO();
        message.setMessagingProduct("message");
        message.setTo(toNumber);
        message.setType("template");

        Template template = new Template();
        template.setName(templateName);
        template.setLanguage(new Language("en_US"));

        message.setTemplate(template);

        System.out.println(message);

        WhatsAppMessageResponseDTO response = postCallBody(apiUrl, WhatsAppMessageResponseDTO.class, message);
        return response;
    }

    private <T> T postCallBody(String apiUrl, Class<T> responseType, Object request) {

        try {
            String response = webClient.post()
                    .uri(apiUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(BodyInserters.fromValue(request))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();


            return objectMapper.readValue(response, responseType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (WebClientResponseException e) {
            String responseBody = e.getResponseBodyAsString();
            System.out.println(responseBody);
            throw new ApiCallError(e.getMessage());
        }
    }
}
