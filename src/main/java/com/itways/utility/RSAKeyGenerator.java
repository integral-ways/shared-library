package com.itways.utility;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RSAKeyGenerator {

//	public static void main(String[] args) {
//		try {
//			// Generate 2048-bit RSA key pair
//			System.out.println("Generating 2048-bit RSA Key Pair:");
//			generateAndPrintKeyPair(2048, true); // With headers/footers
//			System.out.println("\nGenerating 2048-bit RSA Key Pair (No Headers/Footers):");
//			generateAndPrintKeyPair(2048, false); // Without headers/footers
//
//			// Generate 4096-bit RSA key pair
//			System.out.println("\nGenerating 4096-bit RSA Key Pair:");
//			generateAndPrintKeyPair(4096, true); // With headers/footers
//			System.out.println("\nGenerating 4096-bit RSA Key Pair (No Headers/Footers):");
//			generateAndPrintKeyPair(4096, false); // Without headers/footers
//
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//	}

	public static void generateAndPrintKeyPair(int keySize, boolean includeHeaders) throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(keySize);
		KeyPair pair = keyGen.generateKeyPair();

		PrivateKey privateKey = pair.getPrivate();
		PublicKey publicKey = pair.getPublic();

		// Encode keys to Base64
		String privateKeyPEM = Base64.getEncoder().encodeToString(privateKey.getEncoded());
		String publicKeyPEM = Base64.getEncoder().encodeToString(publicKey.getEncoded());

		// Print private key
		System.out.println("Private Key:");
		if (includeHeaders) {
			System.out.println("-----BEGIN RSA PRIVATE KEY-----");
		}
		System.out.println(privateKeyPEM);
		if (includeHeaders) {
			System.out.println("-----END RSA PRIVATE KEY-----");
		}

		// Print public key
		System.out.println("\nPublic Key:");
		if (includeHeaders) {
			System.out.println("-----BEGIN PUBLIC KEY-----");
		}
		System.out.println(publicKeyPEM);
		if (includeHeaders) {
			System.out.println("-----END PUBLIC KEY-----");
		}
	}
}