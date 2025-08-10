package com.itways.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI genericOpenAPI() {
		log.info("✅ Swagger/OpenAPI configuration initialized");

		return new OpenAPI()
				.info(new Info().title("Generic API Documentation")
						.description("This is a generic Swagger setup for any Spring Boot project.").version("v1.0.0")
						.contact(new Contact().name("API Support").url("https://example.com/support")
								.email("support@example.com"))
						.license(new License().name("Apache 2.0").url("https://springdoc.org")))
				.externalDocs(new ExternalDocumentation().description("Project Documentation")
						.url("https://example.com/docs"));
	}
}
