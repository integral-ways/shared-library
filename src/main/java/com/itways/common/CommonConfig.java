package com.itways.common;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ComponentScan(basePackages = "com.itways.common")
@PropertySource("classpath:application-common.properties")
public class CommonConfig {

	@Bean
	public ObjectMapper mapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	@Bean
	public RestTemplate restTemplate() {
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		RestTemplate rest = new RestTemplate(clientHttpRequestFactory());

		return rest;
	}

	private ClientHttpRequestFactory clientHttpRequestFactory() {

		try {
			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

			SSLContext sslContext = SSLContexts.custom().setProtocol("TLSv1.2")
					.loadTrustMaterial(null, acceptingTrustStrategy).build();

			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
			// Allow TLSv1.2 protocol, use NoopHostnameVerifier to trust self-singed cert
//			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
//			        new String[] { "TLSv1" , "TLSv1.1", "TLSv1.2"  }zz, null, new NoopHostnameVerifier());

			HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
					.setSSLSocketFactory(csf).build();

			CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();

			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
					httpClient);
			//
//			requestFactory.setHttpClient();
			return requestFactory;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error SSL Config");
			return null;
		}
	}
}
