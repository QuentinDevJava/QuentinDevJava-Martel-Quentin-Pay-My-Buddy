package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.exception.PasswordEncryptionError;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;

/**
 * Class for encrypting a password. The encryption key is retrieved from the
 * environment variable ENCRYPTION_KEY.
 */
@Component
public class PasswordEncoder {

    private static final Charset UNICODE_FORMAT = StandardCharsets.UTF_8;
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private final Cipher cipher;
    private final SecretKey key;

    /**
     * Initializes the encryption key and scheme.
     *
     * @throws PasswordEncryptionError If the key is missing or invalid.
     */
    public PasswordEncoder(@Value("${encryption.key}") String encryptionKey) throws PasswordEncryptionError {
        String myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        try {
            KeySpec ks = new DESedeKeySpec(encryptionKey.getBytes(UNICODE_FORMAT));
            SecretKeyFactory skf = SecretKeyFactory.getInstance(myEncryptionScheme);
            cipher = Cipher.getInstance(myEncryptionScheme); // NO SONAR
            key = skf.generateSecret(ks);
        } catch (Exception e) {
            throw new PasswordEncryptionError(e.getMessage());
        }
    }

    /**
     * Encrypts a password.
     *
     * @param unencryptedString The password to encrypt.
     * @return The encrypted password.
     */
    public String encrypt(String unencryptedString) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            return new String(Base64.encodeBase64(encryptedText));
        } catch (Exception e) {
            e.printStackTrace(); // TODO mettre un log et remove e.printst....
            throw new PasswordEncryptionError("");
        }
    }
}
