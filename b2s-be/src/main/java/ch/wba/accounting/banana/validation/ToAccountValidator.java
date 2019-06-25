package ch.wba.accounting.banana.validation;

import javax.validation.ConstraintValidatorContext;

import ch.wba.accounting.banana.BananaTransactionDto;

public class ToAccountValidator extends AbstractBananaValidator<ToAccountConstraint> {
    @Override
    public boolean isValidInternal(final BananaTransactionDto transation, final ConstraintValidatorContext context) {
        boolean isValid = true;
        final String toAccount = transation.getCreditAccount();
        if (transation.isIntegrated()) {
            isValid = toAccount == null;
        } else {
            isValid = toAccount != null && !toAccount.isEmpty();
        }
        return isValid;
    }

    @Override
    protected String getValidationField() {
        return "toAccount";
    }
}
