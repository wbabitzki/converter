package ch.wba.accounting.banana.validation;

import ch.wba.accounting.banana.BananaTransactionDto;

import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

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
            BigDecimal vat = value.getAmountVat() != null ? value.getAmountVat() : BigDecimal.ZERO;
            BigDecimal withoutVat = value.getAmountWithoutVat() != null ? value.getAmountWithoutVat() : BigDecimal.ZERO;
            return  value.getAmount() != null &&
                    value.getAmount().abs().subtract(vat.abs()).compareTo(withoutVat.abs()) == 0;
        }
        return true;
    }
}
