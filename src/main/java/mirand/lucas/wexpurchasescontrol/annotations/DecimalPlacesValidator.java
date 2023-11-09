package mirand.lucas.wexpurchasescontrol.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class DecimalPlacesValidator implements ConstraintValidator<DecimalPlaces, BigDecimal> {
    private int decimalPlaces;

    @Override
    public void initialize(DecimalPlaces constraintAnnotation) {
        this.decimalPlaces = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        boolean valid;
        if (value == null) {
            valid = true;
        }
        else {
            valid = value.scale() <= decimalPlaces;
        }

        return valid;
    }
}