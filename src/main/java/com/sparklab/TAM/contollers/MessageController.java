package com.sparklab.TAM.contollers;

import com.sparklab.TAM.dto.message.SmoobuMessageRequest;
import com.sparklab.TAM.exceptions.ApiCallError;
import com.sparklab.TAM.services.ConversationService;
import com.sparklab.TAM.services.MessageService;
import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("TAM/{reservationId}/message/{userId}")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    @PostMapping
    public ResponseEntity<?> sendMessage(@PathVariable int reservationId, @RequestBody SmoobuMessageRequest messageRequest, @PathVariable String userId) {
        try {
            return new ResponseEntity<>(messageService.sendMessage(userId, reservationId, messageRequest), HttpStatus.OK);
        } catch (InternalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping
    public ResponseEntity<?> getMessageByReservationId(@PathVariable int reservationId, @PathVariable String userId) {
        try {
            return new ResponseEntity<>(messageService.getMessageByReservationId(userId, reservationId), HttpStatus.OK);
        } catch (ApiCallError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);

        }
    }
}
