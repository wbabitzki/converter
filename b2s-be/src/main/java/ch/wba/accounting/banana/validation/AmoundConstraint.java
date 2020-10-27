package ch.wba.accounting.banana.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = AmountValidator.class)
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AmoundConstraint {
    String message() default "Amount %s doesnt match to the net %s and tax value %s";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
