package com.itways.common;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.itways.common.props.JwtProperties;

@Configuration
public class RsaKeysConfig {

	@Autowired
	private JwtProperties jwtProperties;

	@Bean
	RSAPrivateKey rsaPrivateKey() {
		try {
			// Remove PEM headers/footers and line breaks
			String privateKeyPem = jwtProperties.getPrivateKey().replaceAll("-----BEGIN (.*)-----", "")
					.replaceAll("-----END (.*)-----", "").replaceAll("\\s+", "");

			byte[] keyBytes = Base64.getDecoder().decode(privateKeyPem);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (Exception ex) {
			return null;
		}
	}

	@Bean
	RSAPublicKey rsaPublicKey() {
		try {
			// Remove PEM headers/footers and whitespace
			String sanitized = jwtProperties.getPublicKey().replaceAll("-----BEGIN (.*)-----", "")
					.replaceAll("-----END (.*)-----", "").replaceAll("\\s+", "");

			byte[] decoded = Base64.getDecoder().decode(sanitized);

			// Use X509EncodedKeySpec for public key
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to create RSA public key", e);
		}
	}

}
