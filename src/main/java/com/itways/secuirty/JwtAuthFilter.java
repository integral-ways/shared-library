package com.itways.secuirty;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.itways.dtos.ApiException;
import com.itways.utility.HashUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JWTVerifier verifier;

	public JwtAuthFilter(RSAPublicKey publicKey) {
		Algorithm algorithm = Algorithm.RSA256(publicKey, null);
		this.verifier = JWT.require(algorithm).build();
		log.info("✅ Secuirty ResourceAuthConfig Enabled");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			try {
				var decodedJWT = verifier.verify(token);

				Map<String, Object> claims = decodedJWT.getClaims().entrySet().stream()
						.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().as(Object.class)));

				decodedJWT.getSubject();
				ItwayUserPrincipal principal = new ItwayUserPrincipal(HashUtil.decode( decodedJWT.getSubject() ), claims);
				
				var auth = new UsernamePasswordAuthenticationToken(principal, null,
						Collections.emptyList());
				
//				auth.setDetails(claims); // store claims here
				SecurityContextHolder.getContext().setAuthentication(auth);
			} catch (Exception e) {
				// token invalid → clear context
				SecurityContextHolder.clearContext();
				throw new ApiException("<<Invalid Authorization>>");
			}
		}
		filterChain.doFilter(request, response);
	}
}
