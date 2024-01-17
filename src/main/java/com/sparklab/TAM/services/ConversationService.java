package com.sparklab.TAM.services;

import com.sparklab.TAM.converters.ConversationToConversationDTO;
import com.sparklab.TAM.converters.ReservationIdToConversation;
import com.sparklab.TAM.dto.reservation.ReservationDTO;
import com.sparklab.TAM.dto.smoobu.ConversationDTO;
import com.sparklab.TAM.model.Conversation;
import com.sparklab.TAM.repositories.ConversationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationToConversationDTO toConversationDTO;
    private final ReservationIdToConversation toConversation;

    public void saveOrUpdate(String userId, ReservationDTO reservation) {

        Conversation conversation = conversationRepository.findByReservationId(reservation.getId())
                .orElse(toConversation.convert(reservation));

        conversationRepository.save(conversation);
    }

    public List<ConversationDTO> getAllConversations(String userId){


        //TODO User Implementation
        Sort sort = Sort.by(Sort.Order.desc("lastChecked"));

        return conversationRepository.findAll(sort)
                .stream()
                .map(toConversationDTO::convert)
                .collect(Collectors.toList());
    }

}
