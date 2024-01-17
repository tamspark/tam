package com.sparklab.TAM.dto.registerandlogin;

import com.sparklab.TAM.model.Role;
import lombok.Data;

@Data
public class RegisterDTO {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;

}
