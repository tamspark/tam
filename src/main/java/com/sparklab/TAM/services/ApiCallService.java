package com.sparklab.TAM.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparklab.TAM.configuration.OpenAIProperties;
import com.sparklab.TAM.exceptions.ApiCallError;
import com.sparklab.TAM.repositories.SmoobuAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@AllArgsConstructor
public class ApiCallService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final SmoobuEncryptionDecryptionService encryptionService;
    private final OpenAIProperties openAIProperties;

    private SmoobuAccountRepository smoobuAccountRepository;

    public <T> T getMethod(String apiUrl, Class<T> responseType,Long userId) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

        try {
            String apiKey = encryptionService.decryptAPIKey(smoobuAccountRepository.findByUser_Id(userId).get().getId()).getClientAPIKey();
//            GET method with Api-Key authentication on header
            String response = webClient.get()
                    .uri(apiUrl)
                    .header("Api-Key", apiKey)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

//            converts the response form string to destination Object/Class
            return objectMapper.readValue(response, responseType);

        } catch (WebClientResponseException e) {
            throw new ApiCallError(e.getStatusCode(), e.getMessage());

//                return example:
//                {
//                    "status":"404", //or other codes
//                    "message":"The requested resource could not be found."
//                }

        } catch (JsonProcessingException e) {
            throw new ApiCallError(HttpStatusCode.valueOf(422), e.getMessage());
        }
    }


    public <T> T postMethod(String apiUrl, Class<T> responseType, Long userId, Object objectDTO) {

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

        try {
            String apiKey = encryptionService.decryptAPIKey(smoobuAccountRepository.findByUser_Id(userId).get().getId()).getClientAPIKey();

            String response = webClient.post()
                    .uri(apiUrl)
                    .header("Api-Key", apiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(objectDTO)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return objectMapper.readValue(response, responseType);

        } catch (WebClientResponseException e) {
            throw new ApiCallError(e.getStatusCode(), e.getMessage());

        } catch (JsonProcessingException e) {
            throw new ApiCallError(HttpStatusCode.valueOf(422), e.getMessage());
        }
    }


    public <T> T postCallMultipartOpenAI(String apiUrl, Class<T> responseType, MultipartBodyBuilder builder) {

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        String apiKey = openAIProperties.getKey();
        try {

            String response = webClient.post()
                    .uri(apiUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return objectMapper.readValue(response, responseType);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }catch (WebClientResponseException e) {
            String responseBody = e.getResponseBodyAsString();
            System.out.println(responseBody);
            throw new ApiCallError(e.getMessage());
        }
    }

    public  <T> T postCallBodyOpenAI(String apiUrl, Class<T> responseType, Object request) {
        String apiKey = openAIProperties.getKey();
        try {
            String response = webClient.post()
                    .uri(apiUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .header("OpenAI-Beta", "assistants=v1")
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

    public  <T> T getCallOpenAI(String apiUrl,  Class<T> responseType) {
        //Refactored
        String apiKey = openAIProperties.getKey();
        try {
            String response = webClient.get()
                    .uri(apiUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .header("OpenAI-Beta", "assistants=v1")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
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


    public <T> T authenticationMethod(String apiUrl, Class<T> responseType, String apiKey) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

        try {

            String response = webClient.get()
                    .uri(apiUrl)
                    .header("Api-Key", apiKey)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return objectMapper.readValue(response, responseType);

        } catch (WebClientResponseException e) {
            String responseBody = e.getResponseBodyAsString();
            System.out.println(responseBody);
            throw new ApiCallError(e.getMessage());
        } catch (JsonProcessingException e) {
            throw new ApiCallError(HttpStatusCode.valueOf(422), e.getMessage());
        }
    }


}
