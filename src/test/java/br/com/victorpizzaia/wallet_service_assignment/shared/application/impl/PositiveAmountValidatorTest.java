package br.com.victorpizzaia.wallet_service_assignment.shared.application.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PositiveAmountValidatorTest {

    private PositiveAmountValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new PositiveAmountValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void testIsValid_withPositiveAmount_returnsTrue() {
        BigDecimal value = new BigDecimal("10.00");
        assertTrue(validator.isValid(value, context));
    }

    @Test
    void testIsValid_withZeroAmount_returnsFalse() {
        BigDecimal value = BigDecimal.ZERO;
        assertFalse(validator.isValid(value, context));
    }

    @Test
    void testIsValid_withNegativeAmount_returnsFalse() {
        BigDecimal value = new BigDecimal("-5.00");
        assertFalse(validator.isValid(value, context));
    }

    @Test
    void testIsValid_withNullValue_returnsFalse() {
        assertFalse(validator.isValid(null, context));
    }
}
