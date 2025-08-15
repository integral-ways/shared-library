package com.itways.config;

import com.itways.aspect.AspectApiResponse;
import com.itways.exception.GlobalExceptionHandler;
import com.itways.security.JwtValidationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({GlobalExceptionHandler.class, AspectApiResponse.class , JwtValidationService.class})
public class SharedLibAutoConfiguration {
}
