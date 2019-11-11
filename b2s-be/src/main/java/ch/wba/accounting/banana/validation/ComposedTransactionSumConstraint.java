package ch.wba.accounting.banana.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ComposedTransactionSumValidator.class)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ComposedTransactionSumConstraint {
    String message() default "Invalid sum of the composed transaction. " +
            "The amount of the main transaction: %s, but the sum of integrated transaction: %s";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
