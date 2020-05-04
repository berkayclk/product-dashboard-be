package com.paydaybank.dashboard.config.datavalidators;

import com.paydaybank.dashboard.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    Logger logger = LoggerFactory.getLogger(UniqueEmailValidator.class);

    @Autowired
    UserRepository userRepository;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {

        try {
            if( email != null && !userRepository.findByEmail(email.toLowerCase().trim()).isPresent() ){
                return true;
            }

        } catch (Exception ex) {
            logger.warn("Email Uniqueness validation error. Error: {} ", ex.getMessage());
        }

        return false;
    }
}
