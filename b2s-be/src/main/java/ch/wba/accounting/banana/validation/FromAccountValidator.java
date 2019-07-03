package ch.wba.accounting.banana.validation;

import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import ch.wba.accounting.banana.BananaTransactionDto;

public class FromAccountValidator extends AbstractBananaValidator<FromAccountConstraint> {
    @Override
    public boolean isValidInternal(final BananaTransactionDto transation, final ConstraintValidatorContext context) {
        final boolean isFromAccountEmpty = Optional.ofNullable(transation.getDebitAccount()) //
            .filter(String::isEmpty) //
            .isPresent();
        if (transation.isComposedTransaction()) {
            return isFromAccountEmpty;
        } else {
            return !isFromAccountEmpty;
        }
    }

    @Override
    protected String getValidationField() {
        return "fromAccount";
    }
}
