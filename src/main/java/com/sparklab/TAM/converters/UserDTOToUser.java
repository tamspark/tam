package com.sparklab.TAM.converters;
import com.sparklab.TAM.dto.UserDTO;
import com.sparklab.TAM.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserDTOToUser implements Converter<UserDTO, User> {
    @Override
    public User convert(UserDTO source) {
        if (source != null) {
            User user = new User();
            user.setId(source.getId());
            user.setEmail(source.getEmail());
            return user;
        }
        return null;
    }


}