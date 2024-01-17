package com.sparklab.TAM.services;

import com.sparklab.TAM.configuration.SmoobuConfiguration;
import com.sparklab.TAM.dto.message.MessageResponseDTO;
import com.sparklab.TAM.dto.message.MessagesResponseDTO;
import com.sparklab.TAM.dto.message.SmoobuMessageRequest;
import com.sparklab.TAM.dto.reservation.ReservationDTO;
import com.sparklab.TAM.exceptions.ApiCallError;
import com.sun.jdi.InternalException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {

    private final ApiCallService apiCallService;
    private final ConversationService conversationService;
    private final SmoobuConfiguration smoobuConfiguration;
    private static final Logger logger = LogManager.getLogger(ChannelService.class);

    public MessageResponseDTO sendMessage(String userId, int reservationId, SmoobuMessageRequest messageRequest) {
        Long parseUserId = Long.parseLong(userId);
        String apiUrl = smoobuConfiguration.getApiURI() + "reservations/" + reservationId + "/messages/send-message-to-guest";
        try {
            return apiCallService.postMethod(apiUrl, MessageResponseDTO.class, parseUserId, messageRequest);
        } catch (ApiCallError e) {
            if (e.getErrorCode() == HttpStatusCode.valueOf(422)) {
                logger.error(e.getMessage(), e);
                throw new ApiCallError(e.getErrorCode(), "Response cannot be parsed as Message object");
            }
            logger.error(e.getMessage(), e);
            throw new InternalException();
        }
    }


    public MessagesResponseDTO getMessageByReservationId(String userId, int reservationId) {
        Long parseUserId = Long.parseLong(userId);
        String apiUrl = smoobuConfiguration.getApiURI() + "reservations/" + reservationId;

        try {
            ReservationDTO reservation = apiCallService.getMethod(apiUrl, ReservationDTO.class, parseUserId);
            conversationService.saveOrUpdate(userId,reservation);

            return apiCallService.getMethod(apiUrl + "/messages", MessagesResponseDTO.class, parseUserId);

        } catch (ApiCallError e) {

            if (e.getErrorCode() == HttpStatusCode.valueOf(422)) {
                logger.error(e.getMessage(), e);
                throw new ApiCallError(e.getErrorCode(), "Response cannot be parsed as Message object");
            }
            logger.error(e.getMessage(), e);
            throw new InternalException();
        }

    }

    public void NewReservationWelcomeMessage(List<ReservationDTO> reservations, Long userId) {
        for (ReservationDTO reservation : reservations) {
            String parsedUserId = String.valueOf(userId);

            conversationService.saveOrUpdate(parsedUserId, reservation);
            SmoobuMessageRequest welcomeMessage = buildReservationWelcomeMessage(reservation);
            String stringUserId = userId.toString();
            try {
                sendMessage(stringUserId, reservation.getId(), welcomeMessage);
            } catch (Exception e) {
                //TODO Suggestion: Send an email to the Admin when this fails
                logger.error(e.getMessage(), e);
            }
        }
    }


    private SmoobuMessageRequest buildReservationWelcomeMessage(ReservationDTO reservation) {
        SmoobuMessageRequest messageRequest = new SmoobuMessageRequest();

        messageRequest.setSubject("Thank you for choosing TAM");
        messageRequest.setMessageBody(
                "<h1>Hello, " + reservation.getGuestName() + "</h1> \n" +
                        "<h2> Thank you for choosing " + reservation.getApartment().getName() + " </h2>" +
                        "<h2>ReservationId: " + reservation.getId() + " </h2>" +
                        "<h2>Reservation StartDate: " + reservation.getArrival() + " </h2>" +
                        "<h2>Reservation EndDate: " + reservation.getDeparture() + " </h2>" +
                        "<h2>ApartmentId to be set on the link: " + reservation.getApartment().getId() + " </h2>" +
                        "<a aria-label=\"Chat on WhatsApp\" href=\"https://wa.me/355683685752\"> <img alt=\"Chat on WhatsApp\" src=\"dataFiles/WhatsAppButtonGreenMedium.svg\" />\n" +
                        "<a />"
        );

        return messageRequest;
    }


}
