package com.sparklab.TAM.services;

import com.sparklab.TAM.dto.registerandlogin.ConfirmationRequest;
import com.sparklab.TAM.dto.registerandlogin.RegisterDTO;
import com.sparklab.TAM.model.User;
import com.sparklab.TAM.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.lang.Boolean.TRUE;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    private String primary_password = "TamSparklab";

    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public RegistrationService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public ResponseEntity<?> registerUser(RegisterDTO registerDTO) throws AddressException {
        User userToSave = new User();
        if (userRepository.existsByEmail(registerDTO.getEmail()) == TRUE) {
            return new ResponseEntity<>("There is already an user with this email address.Please use another email!", HttpStatus.BAD_REQUEST);
        } else {
            userToSave.setFirstName(registerDTO.getFirstName());
            userToSave.setLastName(registerDTO.getLastName());
            userToSave.setEmail(registerDTO.getEmail());
            userToSave.setUsername(registerDTO.getUsername());
            userToSave.setConfirmationToken(generateConfirmationToken());
            userToSave.setPassword(bCryptPasswordEncoder.encode(primary_password));
            userToSave.setTokenCreationDate(LocalDateTime.now());
            userToSave.setRole(registerDTO.getRole());


        }
        try {
            userRepository.save(userToSave);
            String link = "http://localhost:3000/auth/tam/registration/" + userToSave.getConfirmationToken();
            emailService.send(registerDTO.getEmail(), emailService.buildEmail(registerDTO.getFirstName(), link));

        } catch (Exception e) {
            return new ResponseEntity<>("This user can not be saved. Please try again.", HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<>(userToSave.getConfirmationToken(), HttpStatus.OK);
    }

    private String generateConfirmationToken() {
        String confirmationToken = UUID.randomUUID().toString();
        return confirmationToken;
    }
    @Transactional
    public String savePassword(String token, ConfirmationRequest confirmationRequest) {

        User userToUpdatePassword = userRepository.findUserByConfirmationToken(token);

        try {
            if (userToUpdatePassword == null) {
                return "Invalid token. User not found.";
            }
            if (userToUpdatePassword.getTokenConfirmationDate() != null) {
                return "This email has already been confirmed.";
            }
            LocalDateTime expireAt = userToUpdatePassword.getTokenCreationDate().plusHours(24);
            if (expireAt.isBefore(LocalDateTime.now())) {
                userRepository.deleteById(userToUpdatePassword.getId());
                return "The verification link has been expired!";
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return "User password can not be saved.The verification link is expired";

        }
       try {
           String encodedUserPassword = bCryptPasswordEncoder.encode(confirmationRequest.getPassword());
           userToUpdatePassword.setPassword(encodedUserPassword);
           userToUpdatePassword.setTokenConfirmationDate(LocalDateTime.now());
           userToUpdatePassword.setEnabled(true);
           userRepository.save(userToUpdatePassword);
           return "User password was saved successfully.";
       }
       catch(Exception e){
           LOGGER.error(e.getMessage());
           return "An error occurred while saving the user password.";
       }

    }


}
