package com.itways.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.itways.aspect.AspectApiResponse;
import com.itways.exception.GlobalExceptionHandler;

@Configuration
@Import({ GlobalExceptionHandler.class, AspectApiResponse.class })
public class SharedLibAutoConfiguration {

}
