package com.sparklab.TAM.converters;

import com.sparklab.TAM.dto.reservation.ReservationDTO;
import com.sparklab.TAM.model.ProcessedReservations;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReservationDTOToProcessedReservation implements Converter<ReservationDTO, ProcessedReservations> {
    @Override
    public ProcessedReservations convert(ReservationDTO source) {

            ProcessedReservations processedReservations = new ProcessedReservations();
            processedReservations.setSmoobuId(source.getId());

            return processedReservations;
    }
}
