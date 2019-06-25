package ch.wba.accounting.banana.validation;

import javax.validation.ConstraintValidatorContext;

import ch.wba.accounting.banana.BananaTransactionDto;

public class FromAccountValidator extends AbstractBananaValidator<FromAccountConstraint> {
    @Override
    public boolean isValidInternal(final BananaTransactionDto transation, final ConstraintValidatorContext context) {
        final String fromAccount = transation.getDebitAccount();
        if (transation.isComposedTransaction()) {
            return fromAccount == null;
        } else {
            return fromAccount != null && !fromAccount.isEmpty();
        }
    }

    @Override
    protected String getValidationField() {
        return "fromAccount";
    }
}
