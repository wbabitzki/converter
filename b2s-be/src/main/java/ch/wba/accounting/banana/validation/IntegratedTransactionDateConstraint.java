package ch.wba.accounting.banana.validation;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = IntegratedTransactionDateValidator.class)
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegratedTransactionDateConstraint {
    String message() default "Invalid bill date '%s' in the integrated transaction. Date in the main transaction: %s";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
