package com.openclassrooms.payMyBuddy.service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;

import com.openclassrooms.payMyBuddy.exception.PasswordEncryptionError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrippleDes {

	// TODO javadoc ??
	private static final Charset UNICODE_FORMAT = StandardCharsets.UTF_8;
	public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	private KeySpec ks;
	private SecretKeyFactory skf;
	private Cipher cipher;
	byte[] arrayBytes;
	@Value("${encryption.key}")
	private String myEncryptionKey;
	private String myEncryptionScheme;
	SecretKey key;

	public TrippleDes() throws PasswordEncryptionError {
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
