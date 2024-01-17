package com.sparklab.TAM.converters;

import com.sparklab.TAM.dto.UpdateUserDTO;
import com.sparklab.TAM.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUpdateUserDTO implements Converter<User, UpdateUserDTO> {
    @Override
    public UpdateUserDTO convert(User source) {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setId(source.getId());


        updateUserDTO.setFirstName(source.getFirstName());
        updateUserDTO.setLastName(source.getLastName());
        updateUserDTO.setEmail(source.getEmail());
        updateUserDTO.setRole(source.getRole().getName());

        return updateUserDTO;
    }
}
