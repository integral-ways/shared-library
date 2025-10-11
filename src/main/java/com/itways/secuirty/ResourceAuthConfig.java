package com.itways.secuirty;

import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.itways.common.props.SecurityProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ComponentScan(basePackages = "com.itways.secuirty")
@ConditionalOnClass(HttpSecurity.class) // Load only if Spring Security is on the classpath
public class ResourceAuthConfig {

	@Autowired
	private JwtAuthenticationEntryPoint authEntryPoint;

	@Autowired
	private RSAPublicKey publicKey;

	@Autowired
	private SecurityProperties securityProperties;

	@RefreshScope
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
//		.cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅
				// enable
				// CORS
				.exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint)).authorizeHttpRequests(auth -> {
					if (securityProperties.getPublicApis() != null && !securityProperties.getPublicApis().isEmpty()) {
						auth.requestMatchers(securityProperties.getPublicApis().toArray(String[]::new)).permitAll();
					}
					auth.anyRequest().authenticated();
				}).addFilterBefore(new JwtAuthFilter(publicKey), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// ✅ CORS configuration for Security
//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration cors = new CorsConfiguration();
//		cors.setAllowedOriginPatterns(List.of("*")); // allow all origins with credentials
//		cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//		cors.setAllowedHeaders(List.of("*"));
//		cors.setAllowCredentials(true);
//
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", cors);
//		return source;
//	}
}
