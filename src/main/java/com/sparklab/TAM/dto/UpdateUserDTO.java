package com.sparklab.TAM.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    Long id;

    String firstName;
    String lastName;
    String email;
    String role;

    String oldPassword;
    String newPassword;
}
