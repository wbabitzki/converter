package ch.wba.accounting.banana.validation;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ch.wba.accounting.banana.BananaTransactionDto;

public abstract class AbstractBananaValidator<A extends Annotation> implements ConstraintValidator<A, BananaTransactionDto> {
    @Override
    public boolean isValid(final BananaTransactionDto value, final ConstraintValidatorContext context) {
        final boolean isValid = isValidInternal(value, context);
        if (!isValid) {
            customizeViolation(context, getValidationField());
        }
        return isValid;
    }

    protected abstract String getValidationField();

    protected abstract boolean isValidInternal(BananaTransactionDto value, ConstraintValidatorContext context);

    private void customizeViolation(final ConstraintValidatorContext ctx, final String field) {
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate()) //
            .addPropertyNode(field) //
            .addConstraintViolation();
    }
}