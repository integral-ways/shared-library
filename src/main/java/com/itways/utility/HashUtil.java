package com.itways.utility;

import java.nio.ByteBuffer;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import jakarta.xml.bind.DatatypeConverter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HashUtil {
	private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
	private static final int TAG_LENGTH_BIT = 128;
	private static final int IV_LENGTH_BYTE = 12;

	// 256-bit secret key (32 bytes)
	private static SecretKey secretKey;

	// 256-bit secret key (32 bytes)
	// Replace dynamic key generation with a constant or config-loaded key
	private static final String BASE64_KEY = "l9MC7EWWDod5S3ygwJ2ItPUk0sy7rguZJPOD8DTTDdA=";
	static {
		byte[] keyBytes = DatatypeConverter.parseBase64Binary(BASE64_KEY);
		if (keyBytes.length != 32) {
			throw new IllegalArgumentException("Secret key must be 256-bit (32 bytes)");
		}
		secretKey = new SecretKeySpec(keyBytes, "AES");
	}

	// Encrypt/encode long ID
	public static String encode(long id) {
		try {
			byte[] iv = new byte[IV_LENGTH_BYTE];
			new SecureRandom().nextBytes(iv);

			Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
			GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

			byte[] idBytes = ByteBuffer.allocate(Long.BYTES).putLong(id).array();
			byte[] encrypted = cipher.doFinal(idBytes);

			// Combine IV + ciphertext
			ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encrypted.length);
			byteBuffer.put(iv);
			byteBuffer.put(encrypted);
			return DatatypeConverter.printBase64Binary(byteBuffer.array());
		} catch (Exception e) {
			return "";
		}
	}

	// Decrypt/decode back to long ID
	public static long decode(String encoded) {
		try {
			byte[] bytes = DatatypeConverter.parseBase64Binary(encoded);
			ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

			byte[] iv = new byte[IV_LENGTH_BYTE];
			byteBuffer.get(iv);

			byte[] encrypted = new byte[byteBuffer.remaining()];
			byteBuffer.get(encrypted);

			Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
			GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

			byte[] decrypted = cipher.doFinal(encrypted);
			return ByteBuffer.wrap(decrypted).getLong();
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}

	// Utility to generate a random 256-bit key in Base64
	public static String generateBase64Key() {
		byte[] key = new byte[32];
		new SecureRandom().nextBytes(key);
		return DatatypeConverter.printBase64Binary(key);
	}

//	public static void main(String[] args) throws Exception {
//		// Generate secret key once and store securely
//		long originalId = 388L;
//		String encoded = HashUtil.encode(originalId);
//		System.out.println("Encoded ID: " + encoded);
//
//		long decoded = HashUtil.decode(encoded);
//		System.out.println("Decoded ID: " + decoded);
//	}
}
