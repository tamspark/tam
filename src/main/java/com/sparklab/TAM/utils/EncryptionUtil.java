package com.sparklab.TAM.utils;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionUtil {
    private static final String AES_ALGORITHM = "AES";


    public static String encrypt(String apiKey, SecretKey secretKey) {
        try {
            Cipher cipher = null;
            cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = new byte[0];
            encryptedBytes = cipher.doFinal(apiKey.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e.getMessage());
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e.getMessage());
        } catch (BadPaddingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public static String decrypt(String encryptedApiKey, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedApiKey));
            return new String(decryptedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e.getMessage());
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e.getMessage());
        } catch (BadPaddingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}

