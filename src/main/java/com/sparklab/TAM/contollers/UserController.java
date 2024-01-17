package com.sparklab.TAM.contollers;

import com.sparklab.TAM.dto.UpdateUserDTO;
import com.sparklab.TAM.dto.registerandlogin.ResetPasswordDTO;
import com.sparklab.TAM.model.User;
import com.sparklab.TAM.repositories.UserRepository;
import com.sparklab.TAM.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("TAM/")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("findAll")
    private List<User> findAll() {
        return userRepository.findAll();
    }

    @PostMapping("forgetPassword/{email}")
    public ResponseEntity<?> forgetPassword(@PathVariable String email) {
        try {
            return userService.forgetPassword(email);
        } catch (MessagingException e) {
            return new ResponseEntity<>("An error occurred while sending the password reset email.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("resetPassword/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable(value = "token") String token, @RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            return userService.resetPassword(token, resetPasswordDTO);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while resetting the password.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("user/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        return userService.findById(id);
    }

    @PostMapping("user/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDTO updateUserDTO) {
        return userService.updateUser(updateUserDTO);
    }

}
