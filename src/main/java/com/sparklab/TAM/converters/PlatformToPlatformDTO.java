package com.sparklab.TAM.converters;


import com.sparklab.TAM.dto.smoobu.ChannelDTO;
import com.sparklab.TAM.model.Channel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PlatformToPlatformDTO implements Converter<Channel, ChannelDTO> {

    @Override
    public ChannelDTO convert(Channel source) {
        if (source != null) {
            ChannelDTO platformDTO = new ChannelDTO();
            platformDTO.setId(source.getId());
            platformDTO.setName(source.getName());
            return platformDTO;
        }
        return null;
    }
}
