package com.sparklab.TAM.converters;

import com.sparklab.TAM.dto.IOTAuth.IOTAccountDTO;
import com.sparklab.TAM.model.IOTAccount;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IOTAccountToIOTAccountDTO implements Converter<IOTAccount, IOTAccountDTO> {
    @Override
    public IOTAccountDTO convert(IOTAccount source) {
        IOTAccountDTO iotAccountDTO = new IOTAccountDTO();

        iotAccountDTO.setId(source.getId());
        iotAccountDTO.setApiKey(source.getApiKey());
        iotAccountDTO.setSecretKey(source.getSecretKey());
        iotAccountDTO.setApartmentId(source.getApartmentId());

        iotAccountDTO.setUserId(source.getUser().getId());

        return iotAccountDTO;
    }
}
