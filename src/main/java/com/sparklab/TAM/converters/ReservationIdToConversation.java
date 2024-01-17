package com.sparklab.TAM.converters;

import com.sparklab.TAM.dto.reservation.ReservationDTO;
import com.sparklab.TAM.model.Conversation;
import com.sparklab.TAM.services.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@AllArgsConstructor
public class ReservationIdToConversation implements Converter<ReservationDTO, Conversation> {

    @Override
    public Conversation convert(ReservationDTO source) {

        Conversation conversation = new Conversation();

        conversation.setApartmentName(source.getApartment().getName());
        conversation.setGuestName(source.getGuestName());
        conversation.setReservationId(source.getId());
        conversation.setLastChecked(System.currentTimeMillis());

        return conversation;

    }
}
