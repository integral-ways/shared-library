package com.itways.secuirty;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

@Service
@Slf4j
public class RsaService {


    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public RsaService(RSAPrivateKey privateKey, RSAPublicKey publicKey ) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    /**
     * Encrypt plaintext only if RSA is enabled.
     */
    public String encrypt(String data) {

        // if enabled encrypt the data
        try {
            var cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }
        catch (Exception e) {
            throw new RuntimeException("RSA encryption failed", e);
        }
    }

    /**
     * Decrypt ciphertext only if RSA is enabled.
     */
    public String decrypt(String encryptedData) {

        // if enabled decrypt the data
        try {
            var cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = Base64.getDecoder().decode(encryptedData);
            return new String(cipher.doFinal(decryptedBytes));
        }
        catch (Exception e) {
            throw new RuntimeException("RSA decryption failed", e);
        }
    }
}
