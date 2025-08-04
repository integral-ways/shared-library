package com.itways.config;

import com.itways.aspect.AspectApiResponse;
import com.itways.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({GlobalExceptionHandler.class, AspectApiResponse.class})
public class SharedLibAutoConfiguration {
}
