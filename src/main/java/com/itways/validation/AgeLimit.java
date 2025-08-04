package com.itways.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AgeLimitValidator.class)
public @interface AgeLimit {
    String message() default "age must be at least 18";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String age() ;
}
