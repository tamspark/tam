package com.sparklab.TAM.converters;

import com.sparklab.TAM.dto.IOTAuth.IOTAccountDTO;
import com.sparklab.TAM.model.IOTAccount;
import com.sparklab.TAM.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IOTAccountDTOToIOTAccount implements Converter<IOTAccountDTO, IOTAccount> {

    @Override
    public IOTAccount convert(IOTAccountDTO source) {
        IOTAccount iotAccount = new IOTAccount();

        if (source.getId()!=null){
            iotAccount.setId(source.getId());
        }
        iotAccount.setApiKey(source.getApiKey());
        iotAccount.setSecretKey(source.getSecretKey());
        iotAccount.setApartmentId(source.getApartmentId());

        User user = new User();
        user.setId(source.getUserId());

        iotAccount.setUser(user);

        return iotAccount;
    }
}
