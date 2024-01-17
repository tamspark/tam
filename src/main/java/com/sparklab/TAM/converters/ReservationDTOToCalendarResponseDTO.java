package com.sparklab.TAM.converters;

import com.sparklab.TAM.dto.calendar.CalendarResponseDTO;
import com.sparklab.TAM.dto.reservation.ReservationDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReservationDTOToCalendarResponseDTO implements Converter<ReservationDTO, CalendarResponseDTO> {


    @Override
    public CalendarResponseDTO convert(ReservationDTO source) {
        if (source != null) {
            CalendarResponseDTO calendar = new CalendarResponseDTO();
            calendar.setId(source.getId());
            calendar.setArrival(source.getArrival());
            calendar.setDeparture(source.getDeparture());
            calendar.setModifiedAt(source.getModifiedAt());
            calendar.setApartment(source.getApartment());
            calendar.setChannel(source.getChannel());
            calendar.setEmail(source.getEmail());
            calendar.setPhone(source.getPhone());
            calendar.setAdults(source.getAdults());
            calendar.setChildren(source.getChildren());
            calendar.setPrice(source.getPrice());
            calendar.setCreatedAt(source.getCreatedAt());
            calendar.setGuestName(source.getGuestName());
            calendar.setCheckIn(source.getCheckIn());
            calendar.setCheckOut(source.getCheckOut());
            calendar.setPricePaid(source.getPricePaid());
            calendar.setBlocked_booking(source.isBlockedBooking());
            return calendar;
        }
        return null;
    }
}
