package com.sparklab.TAM.converters;

import com.sparklab.TAM.dto.UpdateUserDTO;
import com.sparklab.TAM.exceptions.NotFoundException;
import com.sparklab.TAM.model.User;
import com.sparklab.TAM.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdateUserDTOToUser implements Converter<UpdateUserDTO, User> {
    private final UserRepository userRepository;

    @Override
    public User convert(UpdateUserDTO source) {
        User user = userRepository.findById(source.getId()).orElseThrow(() ->
                new NotFoundException("User with id: " + source.getId() + " does not exist!"));

        user.setId(source.getId());
        if (source.getEmail()!=null){
            user.setEmail(source.getEmail());
        }
        if (source.getFirstName()!=null){
            user.setFirstName(source.getFirstName());
        }
        if (source.getLastName()!=null){
            user.setLastName(source.getLastName());
        }

        return user;

    }
}
