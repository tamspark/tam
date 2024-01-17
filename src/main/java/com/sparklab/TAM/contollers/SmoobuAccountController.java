package com.sparklab.TAM.contollers;


import com.sparklab.TAM.dto.smoobu.SmoobuAccountDTO;
import com.sparklab.TAM.services.AuthenticateSmoobuCredentialsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("TAM/smoobuAccount")
@AllArgsConstructor
public class SmoobuAccountController {


    private final AuthenticateSmoobuCredentialsService authenticateSmoobuCredentials;


    @PostMapping
    public ResponseEntity authenticateSmoobuCredentials(@RequestBody SmoobuAccountDTO smoobuAccountDTO) {
        try {
            return new ResponseEntity<>(authenticateSmoobuCredentials.authenticateSmoobuCredentials(smoobuAccountDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
