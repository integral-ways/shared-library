package com.itways.secuirty;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SSLPrepare {

	public static void prepare() {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { new TrustAllManager(), new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} }, new SecureRandom());
			SSLContext.setDefault(sslContext);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}

	public static void addCertificates() {

		try {
			// Load the certificate from the 'certificates' folder inside resources
			InputStream certInputStream = SSLPrepare.class.getClassLoader()
					.getResourceAsStream("certificates/inma-internal-uat.crt");

			if (certInputStream == null) {
				throw new FileNotFoundException(
						"Certificate file not found in classpath at 'certificates/inma-internal-uat.crt'.");
			}

			// Using the newer Java 21 syntax for loading and processing certificates
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(certInputStream);

			// Load the default trust store (cacerts)
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null); // Load the empty truststore

			// Add the certificate to the trust store
			trustStore.setCertificateEntry("eureka-server", certificate);

			// Create a TrustManager that trusts the certificate
			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(trustStore);
			TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

			// Set up the SSL context with the TrustManager
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, trustManagers, new SecureRandom());

			// Set the default SSL context to the one we created
			SSLContext.setDefault(sslContext);

			System.out.println("SSL CERT is ADDED");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
