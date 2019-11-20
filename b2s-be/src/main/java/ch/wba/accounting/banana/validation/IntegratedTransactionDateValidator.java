package ch.wba.accounting.banana.validation;

import javax.validation.ConstraintValidatorContext;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.converters.LocalDateConverter;

public class IntegratedTransactionDateValidator extends AbstractBananaValidator<IntegratedTransactionDateConstraint> {

    static final String FIELD_DATE = "date";

    @Override
    public String getMessageTemplate(final BananaTransactionDto dto, final ConstraintValidatorContext ctx) {
        return String.format(ctx.getDefaultConstraintMessageTemplate(),
                dto.getDate() != null ? LocalDateConverter.toString(dto.getDate()) : null,
                dto.getMainTransaction() != null && dto.getMainTransaction().getDate() != null ?
                        LocalDateConverter.toString(dto.getMainTransaction().getDate()) : null);
    }
    @Override
    protected String getValidationField() {
        return FIELD_DATE;
    }

    @Override
    protected boolean isValidInternal(final BananaTransactionDto value, ConstraintValidatorContext context) {
        if (value.getMainTransaction() == null) {
            return true;
        }
        return value.getDate() != null && value.getDate().equals(value.getMainTransaction().getDate());
    }
}
