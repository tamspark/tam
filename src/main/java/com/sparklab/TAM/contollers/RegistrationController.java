package com.sparklab.TAM.contollers;

import com.sparklab.TAM.dto.registerandlogin.ConfirmationRequest;
import com.sparklab.TAM.dto.registerandlogin.RegisterDTO;
import com.sparklab.TAM.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.AddressException;

@RestController
@RequestMapping("/TAM")
@AllArgsConstructor
public class RegistrationController {


    private final RegistrationService registrationService;

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO) {
        try {
            return registrationService.registerUser(registerDTO);
        } catch (AddressException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("savepassword/{token}")
    public String savePassword(@PathVariable("token") String token, @RequestBody ConfirmationRequest confirmationRequest) {
       try {
           return registrationService.savePassword(token, confirmationRequest);
       } catch(IllegalArgumentException  e){
           throw new IllegalArgumentException(e.getMessage());
       }
    }

}
