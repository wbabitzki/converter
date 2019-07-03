package ch.wba.accounting.banana.validation;

import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import ch.wba.accounting.banana.BananaTransactionDto;

public class ToAccountValidator extends AbstractBananaValidator<ToAccountConstraint> {
    @Override
    public boolean isValidInternal(final BananaTransactionDto transation, final ConstraintValidatorContext context) {
        final boolean isToAccountEmpty = Optional.ofNullable(transation.getCreditAccount()) //
            .filter(String::isEmpty) //
            .isPresent();
        if (transation.isIntegrated()) {
            return isToAccountEmpty;
        } else {
            return !isToAccountEmpty;
        }
    }

    @Override
    protected String getValidationField() {
        return "toAccount";
    }
}
