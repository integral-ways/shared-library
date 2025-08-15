package com.itways.secuirty;

import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable).exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/public/**", "/auth/**", "/swagger-ui/**", "/swagger-ui.html",
								"/v3/api-docs/**", "/swagger-resources/**", "/webjars/**")
						.permitAll().anyRequest().authenticated())
				.addFilterBefore(new JwtAuthFilter(publicKey), UsernamePasswordAuthenticationFilter.class);

//		log.info("✅ Secuirty ResourceAuthConfig Enabled");
		return http.build();
	}
}
