package com.sparklab.TAM.services;

import com.sparklab.TAM.converters.UpdateUserDTOToUser;
import com.sparklab.TAM.converters.UserToUpdateUserDTO;
import com.sparklab.TAM.dto.UpdateUserDTO;
import com.sparklab.TAM.dto.registerandlogin.ResetPasswordDTO;
import com.sparklab.TAM.exceptions.NotFoundException;
import com.sparklab.TAM.model.User;
import com.sparklab.TAM.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {
    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 60;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UpdateUserDTOToUser toUser;
    private final UserToUpdateUserDTO toUpdateUserDTO;
    private CustomUserDetailsService customUserDetailsService;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);


    public UserService(UserRepository userRepository, EmailService emailService, BCryptPasswordEncoder bCryptPasswordEncoder, UpdateUserDTOToUser toUser, UserToUpdateUserDTO toUpdateUserDTO, CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.toUser = toUser;
        this.toUpdateUserDTO = toUpdateUserDTO;
        this.customUserDetailsService = customUserDetailsService;
    }

    private String generateToken() {
        UUID uuid = UUID.randomUUID();
        return String.valueOf(uuid);
    }

    private boolean tokenIsExpired(final LocalDateTime tokenCreationDateForgotPassword) {
        LocalDateTime now = LocalDateTime.now();
        Duration difference = Duration.between(tokenCreationDateForgotPassword, now);
        return difference.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;

    }

    public ResponseEntity<?> forgetPassword(String email) throws MessagingException {
        try {
            Optional<User> userOptional = userRepository.findUsersByEmail(email);
            if (!userOptional.isPresent()) {
                return new ResponseEntity<>("There is no user with this email address.", HttpStatus.BAD_REQUEST);
            }

            User user = userOptional.get();
            user.setForgetPasswordToken(generateToken());
            user.setForgetPasswordTokenCreationDate(LocalDateTime.now());
            user = userRepository.save(user);

            String link = "http://localhost:3000/TAM/resetPassword/" + user.getForgetPasswordToken();

            if (customUserDetailsService.exists(email)) {
                emailService.send(user.getEmail(), emailService.buildResetEmail(user.getFirstName(), link));
            }
            return new ResponseEntity<>(user.getForgetPasswordToken(), HttpStatus.OK);
        } catch (DataAccessException e) {
            LOGGER.error("Database error while processing forgetPassword: " + e.getMessage());
            return new ResponseEntity<>("A database error occurred while processing the request.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            LOGGER.error("Error in forgetPassword: " + e.getMessage());
            return new ResponseEntity<>("An error occurred while processing the password reset request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> resetPassword(String uuid, ResetPasswordDTO resetPasswordDTO) {
        try {
            Optional<User> optionalUser = Optional.ofNullable(userRepository.findByForgetPasswordToken(uuid));
            if (!optionalUser.isPresent()) {
                return new ResponseEntity<>("The redirection link is invalid ", HttpStatus.NOT_ACCEPTABLE);
            }

            LocalDateTime tokenCreationDate = optionalUser.get().getForgetPasswordTokenCreationDate();
            if (tokenIsExpired(tokenCreationDate)) {
                return new ResponseEntity<>("The link has expired. Please complete once again the forgot password form.", HttpStatus.NOT_ACCEPTABLE);
            }

            User user = optionalUser.get();
            user.setPassword(bCryptPasswordEncoder.encode(resetPasswordDTO.getPassword()));
            user.setForgetPasswordToken(null);
            userRepository.save(user);
            return new ResponseEntity<>("Your password was successfully changed!", HttpStatus.OK);

        } catch (DataAccessException e) {
            LOGGER.error("Database error while processing resetPassword: " + e.getMessage());
            return new ResponseEntity<>("A database error occurred while processing the request.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            LOGGER.error("Unexpected error in resetPassword: " + e.getMessage());
            return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<?> updateUser(UpdateUserDTO updateUserDTO) {
        try {
            User user = toUser.convert(updateUserDTO);

            if (updateUserDTO.getOldPassword()!=null &&  bCryptPasswordEncoder.matches(updateUserDTO.getOldPassword(), user.getPassword())) {
                user.setPassword(bCryptPasswordEncoder.encode(updateUserDTO.getNewPassword()));
            }

            userRepository.save(user);

            return new ResponseEntity<>(toUpdateUserDTO.convert(user), HttpStatus.OK);

        } catch (DataAccessException e) {
            LOGGER.error("Database error while processing resetPassword: " + e.getMessage());
            return new ResponseEntity<>("A database error occurred while processing the request.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            LOGGER.error("Unexpected error in resetPassword: " + e.getMessage());
            return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> findById(Long id) {
        try {

            User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
            UpdateUserDTO updateUserDTO = toUpdateUserDTO.convert(user);

            return new ResponseEntity<>(updateUserDTO,HttpStatus.OK);
        }catch (NotFoundException e){
            return new ResponseEntity<>("User with id: " + id + "does not exist!",HttpStatus.BAD_REQUEST);
        }
    }
}
