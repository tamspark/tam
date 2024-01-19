package com.sparklab.TAM.services;

import com.sparklab.TAM.dto.smoobu.SmoobuResponseDTO;
import com.sparklab.TAM.dto.smoobu.SmoobuAccountDTO;
import com.sparklab.TAM.exceptions.DuplicateException;
import com.sparklab.TAM.exceptions.NotFoundException;
import com.sparklab.TAM.model.SmoobuAccount;
import com.sparklab.TAM.model.User;
import com.sparklab.TAM.repositories.SmoobuAccountRepository;
import com.sparklab.TAM.repositories.UserRepository;
import com.sparklab.TAM.utils.EncryptionUtil;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;


@Service
@AllArgsConstructor
public class SmoobuEncryptionDecryptionService {

    private final SmoobuAccountRepository smoobuAccountRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LogManager.getLogger(SmoobuEncryptionDecryptionService.class);

    public SecretKey generateSecretKey() {
        try {
            Map<String, String> env = System.getenv();

            byte[] secretKeyBytes = env
                    .get("MY_SECRET_KEY")
                    .getBytes(StandardCharsets.UTF_8);
            MessageDigest shaMessage = MessageDigest.getInstance("SHA-256");
            secretKeyBytes = shaMessage.digest(secretKeyBytes);
            secretKeyBytes = Arrays.copyOf(secretKeyBytes, 16);
            return new SecretKeySpec(secretKeyBytes, "AES");

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating AES key from string: " + e.getMessage(), e);
        }

    }


    public SmoobuResponseDTO encryptAPIKey(SmoobuAccountDTO smoobuAccountDTO) {
        try {

            SecretKey secretKey = generateSecretKey();
            String encryptedApiKey = EncryptionUtil.encrypt(smoobuAccountDTO.getClientAPIKey(), secretKey);
            String encryptedClientId = EncryptionUtil.encrypt(smoobuAccountDTO.getClientId(), secretKey);
            if (!smoobuAccountRepository.existsByClientId(encryptedClientId)) {
                User smoobuUser=userRepository.findById(smoobuAccountDTO.getUserId()).get();
                SmoobuAccount apikey = new SmoobuAccount(encryptedClientId, encryptedApiKey,smoobuUser);
                smoobuAccountRepository.save(apikey);
                return new SmoobuResponseDTO(smoobuAccountRepository.existsByUser_Id(smoobuAccountDTO.getUserId()));
            }
            throw new DuplicateException("The Client ID :" + smoobuAccountDTO.getClientId() + " you provided already exists. Please use a different Client ID.");
        } catch (DuplicateException e) {
            throw new RuntimeException(e.getMessage());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("There was an error processing the client ID and its SecretKey");
        }
    }


    public SmoobuAccount decryptAPIKey(Long id) {
        try {
            SmoobuAccount apikey = smoobuAccountRepository.findById(id).get();
            SecretKey decryptKey = generateSecretKey();
            String decryptedApiKey = EncryptionUtil.decrypt(apikey.getClientAPIKey(), decryptKey);
            String decryptedClientId = EncryptionUtil.decrypt(apikey.getClientId(), decryptKey);

            SmoobuAccount decryptedAccount = new SmoobuAccount();
            decryptedAccount.setId(apikey.getId());
            decryptedAccount.setClientAPIKey(decryptedApiKey);
            decryptedAccount.setClientId(decryptedClientId);

            return decryptedAccount;

//            apikey.setClientAPIKey(decryptedApiKey);
//            apikey.setClientId(decryptedClientId);
//            return apikey;
        } catch (NoSuchElementException e) {
            throw new NotFoundException("No API Key and SecretKey Found For this Client");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InternalError("An error occurred API KEY could not be decrypted");
        }
    }


}
