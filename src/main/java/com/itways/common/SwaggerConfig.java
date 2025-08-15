package com.itways.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SwaggerConfig {

	@Value("${service.name:Generic API}")
	private String serviceName;

	@Value("${authentication.type:jwt}") // default to jwt
	private String authType;

	private static final String SECURITY_SCHEME_NAME = "authScheme";

	@Bean
	public OpenAPI genericOpenAPI() {
		log.info("✅ Swagger/OpenAPI configuration initialized with serviceName='{}', authType='{}'", serviceName,
				authType);

		OpenAPI openAPI = new OpenAPI()
				.info(new Info().title(serviceName + " API Documentation")
						.description("This is the Swagger setup for " + serviceName).version("v1.0.0")
						.contact(new Contact().name("API Support").url("https://itways.com/support")
								.email("support@itways.com"))
						.license(new License().name("Apache 2.0").url("https://springdoc.org")))
				.externalDocs(new ExternalDocumentation().description("Project Documentation")
						.url("https://itways.com/docs"));

		Components components = new Components();
		SecurityRequirement securityRequirement = new SecurityRequirement();

		if ("oauth2".equalsIgnoreCase(authType)) {
			// OAuth2 config - example with authorizationCode flow
			OAuthFlows flows = new OAuthFlows().authorizationCode(new OAuthFlow()
					.authorizationUrl("https://example.com/oauth/authorize").tokenUrl("https://example.com/oauth/token")
					.scopes(new Scopes().addString("read", "Read access").addString("write", "Write access")));

			SecurityScheme oauth2Scheme = new SecurityScheme().type(SecurityScheme.Type.OAUTH2)
					.description("OAuth2 Authorization Code Flow").flows(flows);

			components.addSecuritySchemes(SECURITY_SCHEME_NAME, oauth2Scheme);
			securityRequirement.addList(SECURITY_SCHEME_NAME, java.util.List.of("read", "write"));

		} else if ("jwt".equalsIgnoreCase(authType)) {
			// JWT Bearer config
			SecurityScheme jwtScheme = new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
					.bearerFormat("JWT").description("JWT Bearer token authentication");

			components.addSecuritySchemes(SECURITY_SCHEME_NAME, jwtScheme);
			securityRequirement.addList(SECURITY_SCHEME_NAME);
		} else {
			log.warn("Unknown authentication.type '{}', no security scheme applied in Swagger", authType);
		}

		if (!securityRequirement.isEmpty()) {
			openAPI.components(components).addSecurityItem(securityRequirement);
		}

		return openAPI;
	}
}
