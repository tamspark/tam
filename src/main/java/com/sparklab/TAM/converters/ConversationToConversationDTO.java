package com.sparklab.TAM.converters;

import com.sparklab.TAM.dto.smoobu.ConversationDTO;
import com.sparklab.TAM.model.Conversation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ConversationToConversationDTO implements Converter<Conversation, ConversationDTO> {
    @Override
    public ConversationDTO convert(Conversation source) {
        ConversationDTO conversationDTO = new ConversationDTO();

        conversationDTO.setId(source.getId());
        conversationDTO.setGuestName(source.getGuestName());
        conversationDTO.setApartmentName(source.getApartmentName());
        conversationDTO.setReservationId(source.getReservationId());
        conversationDTO.setLastChecked(source.getLastChecked());

        return conversationDTO;
    }
}
