package com.itways.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class AgeLimitValidator implements ConstraintValidator<AgeLimit, Object> {
    private String ageFieldName;
    private String messageFieldName;

    @Override
    public void initialize(AgeLimit ageLimit) {
        this.ageFieldName = ageLimit.age();
        this.messageFieldName = ageLimit.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
            Integer age = (Integer) beanWrapper.getPropertyValue(ageFieldName);
            if (age != null && age < 18) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(messageFieldName)
                        .addPropertyNode(ageFieldName)
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
