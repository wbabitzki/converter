package ch.wba.accounting.banana.validation;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.converters.BigDecimalConverter;

import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.Objects;

public class ComposedTransactionSumValidator extends AbstractBananaValidator<ComposedTransactionSumConstraint>{

    static final String FIELD_AMOUNT = "amount";

    @Override
    public String getMessageTemplate(ConstraintValidatorContext ctx, BananaTransactionDto dto) {
        BigDecimal sum = sumAmountOfIntegratedTransactions(dto);
        return String.format(ctx.getDefaultConstraintMessageTemplate(), //
                dto.getAmount() == null ? "0.00" : BigDecimalConverter.asString(dto.getAmount()), //
                sum == null ? "0.00" : BigDecimalConverter.asString(sum));
    }

    @Override
    protected boolean isValidInternal(BananaTransactionDto value, ConstraintValidatorContext context) {
        if (value.isComposedTransaction()) {
            return isAmountNotNull(value) && isAmountEqualsToSumOfIntegratedTransactions(value);
        } else {
            return true;
        }
    }

    @Override
    protected String getValidationField() {
        return FIELD_AMOUNT;
    }

    private boolean isAmountEqualsToSumOfIntegratedTransactions(BananaTransactionDto value) {
        BigDecimal sumOfIntegratedTransactions = sumAmountOfIntegratedTransactions(value);
        return value.getAmount().compareTo(sumOfIntegratedTransactions) == 0;
    }

    private BigDecimal sumAmountOfIntegratedTransactions(BananaTransactionDto dto) {
        return dto.getIntegratedTransactions()
                    .stream()
                    .map(BananaTransactionDto::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isAmountNotNull(BananaTransactionDto value) {
        return value.getAmount() != null;
    }
}
