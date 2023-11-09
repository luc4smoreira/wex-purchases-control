package mirand.lucas.wexpurchasescontrol.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DecimalPlacesValidator.class)
public @interface DecimalPlaces {
    String message() default "Invalid number of decimal places";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int value();
}
