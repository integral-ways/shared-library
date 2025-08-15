package com.itways.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startFieldName;
    private String endFieldName;
    private String message;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startFieldName = constraintAnnotation.start() ;
        this.endFieldName = constraintAnnotation.end() ;
        this.message = constraintAnnotation.message() ;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
            LocalDate startDate = (LocalDate) beanWrapper.getPropertyValue(startFieldName);
            LocalDate endDate = (LocalDate) beanWrapper.getPropertyValue(endFieldName);

            if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(startFieldName)
                        .addConstraintViolation();
                return false;
            }

            if (startDate != null && endDate != null && startDate.isEqual(endDate)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(endFieldName)
                        .addConstraintViolation();
                return false;
            }

        }
        catch (Exception e) {
            return false;
}
        return true;
    }
}