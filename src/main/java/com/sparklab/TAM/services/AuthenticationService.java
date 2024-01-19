package com.sparklab.TAM.services;

import com.sparklab.TAM.dto.registerandlogin.LoginResponse;
import com.sparklab.TAM.dto.registerandlogin.LoginDTO;
import com.sparklab.TAM.exceptions.LogoutFailedException;
import com.sparklab.TAM.model.User;
import com.sparklab.TAM.repositories.SmoobuAccountRepository;
import com.sparklab.TAM.repositories.UserRepository;
import com.sparklab.TAM.utils.JwtUtils;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private SmoobuAccountRepository smoobuAccountRepository;
    private final JwtUtils jwtUtils;
    private HttpSession session;

    private final AuthenticationProvider authenticationManager;


    public Object login(LoginDTO loginDTO) {

        LoginResponse loginResponse;
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getEmail(), loginDTO.getPassword()));
            System.out.println(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtils.generateAccessToken(userRepository.findUserByEmailAndIsEnabledTrue(loginDTO.getEmail()).get());
           loginResponse = setUserData(userRepository.findUserByEmailAndIsEnabledTrue(loginDTO.getEmail()).get(), token);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Login failed ! Invalid email or password.");
        } catch (Exception e) {
            throw new InternalError("We encountered an issue while processing your request. Please try again later.Thank you for your understanding.");
        }
    }


    public LoginResponse setUserData(User user, String token) {
        LoginResponse apiResponse = new LoginResponse();
        apiResponse.setId(user.getId());
        apiResponse.setEmail(user.getEmail());
        apiResponse.setFirstName(user.getFirstName());
        apiResponse.setLastName(user.getLastName());
        apiResponse.setRole(user.getRole().getName());
        apiResponse.setAccessToken(token);
        apiResponse.setRegistredInSmoobu(smoobuAccountRepository.existsByUser_Id(user.getId()));
        return apiResponse;
    }


    public String logout(String id) {

        try {
            Long parseId = Long.parseLong(id);
            if (parseId != null && userRepository.existsById(parseId)) {
                session.invalidate();
                SecurityContextHolder.clearContext();
                return "Logout successful. You have been successfully logged out.";
            }
            throw new LogoutFailedException("Logout failed. No user exists with this id");
        } catch (NumberFormatException e) {
            throw new NumberFormatException("User id: \"" + id + "\" can't be parsed!");
        } catch (Exception e) {
            throw new LogoutFailedException("Logout failed. Please try again later.");
        }
    }
}
