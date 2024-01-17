package com.sparklab.TAM.services;

import com.sparklab.TAM.configuration.SmoobuConfiguration;
import com.sparklab.TAM.dto.smoobu.SmoobuResponseDTO;
import com.sparklab.TAM.dto.smoobu.SmoobuAccountDTO;
import com.sparklab.TAM.dto.registerandlogin.UserAuthenticationSmoobuDTO;
import com.sparklab.TAM.exceptions.ApiCallError;
import com.sun.jdi.InternalException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticateSmoobuCredentialsService {

    private final ApiCallService apiCallService;
    private final SmoobuConfiguration smoobuConfiguration;
    private final SmoobuEncryptionDecryptionService smoobuEncryptionDecryptionService;
    private static final Logger logger = LogManager.getLogger(AuthenticateSmoobuCredentialsService.class);


    public SmoobuResponseDTO authenticateSmoobuCredentials(SmoobuAccountDTO smoobuAccountDTO) {
        try {
            String apiUrl = smoobuConfiguration.getApiURI() + "me";
            UserAuthenticationSmoobuDTO userAuthenticationSmoobuDTO = apiCallService.authenticationMethod(apiUrl, UserAuthenticationSmoobuDTO.class, smoobuAccountDTO.getClientAPIKey());
            if (Integer.parseInt(smoobuAccountDTO.getClientId()) == (userAuthenticationSmoobuDTO.getId())) {
                return smoobuEncryptionDecryptionService.encryptAPIKey(smoobuAccountDTO);

            }
            throw new ApiCallError("Authentication failed for the provided Smoobu credentials. Please ensure that you have entered your Smoobu credentials correctly.");
        } catch (ApiCallError e) {
            logger.error(e.getMessage(), e);
            throw new InternalException("Authentication failed for the provided Smoobu credentials. Please ensure that you have entered your Smoobu credentials correctly.");
        }
        catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            throw new InternalException("Authentication failed for the provided Smoobu credentials. Please ensure that you have entered your Smoobu credentials correctly.");
        }
    }
}
