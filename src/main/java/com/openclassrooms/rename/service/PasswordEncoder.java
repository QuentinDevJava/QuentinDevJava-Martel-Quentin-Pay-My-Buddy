package com.openclassrooms.rename.service;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import com.openclassrooms.rename.exception.PasswordEncryptionError;

/**
 * Class for encrypting a password. The encryption key is retrieved from the
 * environment variable ENCRYPTION_KEY.
 */
@Component
public class PasswordEncoder {

	private static final Charset UNICODE_FORMAT = StandardCharsets.UTF_8;
	public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	private KeySpec ks;
	private SecretKeyFactory skf;
	private Cipher cipher;
	byte[] arrayBytes;
	private String myEncryptionKey;
	private String myEncryptionScheme;
	SecretKey key;

	/**
	 * Initializes the encryption key and scheme.
	 * 
	 * @throws PasswordEncryptionError If the key is missing or invalid.
	 */
	public PasswordEncoder() throws PasswordEncryptionError {
		myEncryptionKey = System.getenv("ENCRYPTION_KEY");
		if (myEncryptionKey == null || myEncryptionKey.isEmpty()) {
			throw new PasswordEncryptionError("Encryption key is missing.");
		}
		myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
		arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
		try {
			ks = new DESedeKeySpec(arrayBytes);
			skf = SecretKeyFactory.getInstance(myEncryptionScheme);
			cipher = Cipher.getInstance(myEncryptionScheme);
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
		String encryptedString = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
			byte[] encryptedText = cipher.doFinal(plainText);
			encryptedString = new String(Base64.encodeBase64(encryptedText));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedString;
	}
}
