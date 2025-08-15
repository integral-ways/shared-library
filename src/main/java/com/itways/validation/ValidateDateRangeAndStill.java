package com.itways.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidDateRangeAndStill.class)
@Documented

public @interface ValidateDateRangeAndStill {
    String message() default "Invalid date range";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String start() ;

    String end() ;

    String isCurrent() ;

}
