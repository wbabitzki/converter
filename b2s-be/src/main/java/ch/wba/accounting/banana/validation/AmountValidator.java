package ch.wba.accounting.banana.validation;

import ch.wba.accounting.banana.BananaTransactionDto;

import javax.validation.ConstraintValidatorContext;

public class AmountValidator extends AbstractBananaValidator<AmoundConstraint> {
    static final String FIELD_AMOUNT = "amount";

    @Override
    public String getMessageTemplate(BananaTransactionDto dto, ConstraintValidatorContext ctx) {
        return String.format(ctx.getDefaultConstraintMessageTemplate(), //
                dto.getAmount() == null ? "0" :dto.getAmount(),
                dto.getAmountWithoutVat() == null ? "0" : dto.getAmountWithoutVat(),
                dto.getAmountVat() == null ? "0" : dto.getAmountVat());
    }

    @Override
    protected String getValidationField() {
        return FIELD_AMOUNT;
    }

    @Override
    protected boolean isValidInternal(BananaTransactionDto value, ConstraintValidatorContext context) {
        if (value.getAmountVat() != null || value.getAmountWithoutVat() != null) {
            return  value.getAmount() != null &&
                    value.getAmount().subtract(value.getAmountVat().abs()).compareTo(value.getAmountWithoutVat()) == 0;
        }
        return true;
    }
}
