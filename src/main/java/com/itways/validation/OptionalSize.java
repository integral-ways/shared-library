package com.itways.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OptionalSizeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalSize {
    String message() default "invalid size";

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
