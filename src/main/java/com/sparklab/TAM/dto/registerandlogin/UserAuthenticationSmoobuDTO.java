package com.sparklab.TAM.dto.registerandlogin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAuthenticationSmoobuDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String email;


}
