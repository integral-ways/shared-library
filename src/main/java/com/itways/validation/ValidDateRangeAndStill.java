package com.itways.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDate;

public class ValidDateRangeAndStill implements ConstraintValidator<ValidateDateRangeAndStill, Object> {

    private String startFieldName;
    private String endFieldName;
    private String message;
    private String isCurrentJobFieldName;

    @Override
    public void initialize(ValidateDateRangeAndStill constraintAnnotation) {
        this.startFieldName = constraintAnnotation.start() ;
        this.endFieldName = constraintAnnotation.end();
        this.message = constraintAnnotation.message();
        this.isCurrentJobFieldName = constraintAnnotation.isCurrent();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value) ;
            LocalDate startDate = (LocalDate) beanWrapper.getPropertyValue(startFieldName) ;
            LocalDate endDate = (LocalDate) beanWrapper.getPropertyValue(endFieldName) ;
            Object val = beanWrapper.getPropertyValue(isCurrentJobFieldName) ;
            boolean isCurrentJob = val != null && Boolean.TRUE.equals(val) ;
            context.disableDefaultConstraintViolation();

            if (startDate == null){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("start date is required")
                        .addPropertyNode(startFieldName)
                        .addConstraintViolation();
                return false ;
            }

            if (isCurrentJob){
                if (endDate != null){
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(message)
                            .addPropertyNode(endFieldName)
                            .addConstraintViolation();
                    return false;
                }
                return true ;
            }
            if (endDate == null){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("end date is required for past")
                        .addPropertyNode(endFieldName)
                        .addConstraintViolation();
                return false;
            }
            if (startDate.equals(endDate)){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("end date must be after start date")
                        .addPropertyNode(endFieldName)
                        .addConstraintViolation();
                return false ;
            }

            if ( endDate.isBefore(startDate)){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("end date must be after start date")
                        .addPropertyNode(endFieldName)
                        .addConstraintViolation();
                return false ;
            }
            return true ;
        }
        catch (Exception e){
            e.printStackTrace();
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Un expected error occurred" + e.getMessage())
                    .addConstraintViolation();
            return false ;
        }
    }
}
