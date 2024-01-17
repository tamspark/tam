package com.sparklab.TAM.converters;


import com.sparklab.TAM.dto.smoobu.ChannelDTO;
import com.sparklab.TAM.model.Channel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PlatformDTOToPlatform implements Converter<ChannelDTO, Channel> {

    @Override
    public Channel convert(ChannelDTO source) {
        if (source != null) {
            Channel platform = new Channel();
            if (source.getId() != null) {
                platform.setId(source.getId());
            }
            platform.setName(source.getName());
            return platform;
        }
        return null;
    }
}
