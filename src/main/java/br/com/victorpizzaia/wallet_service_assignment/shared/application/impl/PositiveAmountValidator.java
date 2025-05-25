package br.com.victorpizzaia.wallet_service_assignment.shared.application.impl;

import java.math.BigDecimal;

import br.com.victorpizzaia.wallet_service_assignment.shared.application.PositiveAmount;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PositiveAmountValidator implements ConstraintValidator<PositiveAmount, BigDecimal> {

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }
}
