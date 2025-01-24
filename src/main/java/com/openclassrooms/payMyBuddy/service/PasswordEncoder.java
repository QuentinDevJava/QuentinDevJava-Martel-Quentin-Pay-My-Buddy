package com.openclassrooms.payMyBuddy.service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import com.openclassrooms.payMyBuddy.exception.PasswordEncryptionError;

/**
 * The Class PasswordEncoder.
 */
//TODO a verifier et javadoc a faire 
@Component
public class PasswordEncoder {
	
	/** The Constant UNICODE_FORMAT. */
	private static final Charset UNICODE_FORMAT = StandardCharsets.UTF_8;
	
	/** The Constant DESEDE_ENCRYPTION_SCHEME. */
	public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	
	/** The ks. */
	private KeySpec ks;
	
	/** The skf. */
	private SecretKeyFactory skf;
	
	/** The cipher. */
	private Cipher cipher;
	
	/** The array bytes. */
	byte[] arrayBytes;
	
	/** The my encryption key. */
	private String myEncryptionKey;
	
	/** The my encryption scheme. */
	private String myEncryptionScheme;
	
	/** The key. */
	SecretKey key;

	/**
	 * Instantiates a PasswordEncoder .
	 *
	 * @throws PasswordEncryptionError the password encryption error
	 */
	public PasswordEncoder() throws PasswordEncryptionError {
		myEncryptionKey = "ThisIsASecureKeyForProtectPassword"; 
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
	 * Encrypt.
	 *
	 * @param unencryptedString the unencrypted string
	 * @return the string
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
