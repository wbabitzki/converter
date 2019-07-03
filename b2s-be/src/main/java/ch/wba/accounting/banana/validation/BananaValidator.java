package ch.wba.accounting.banana.validation;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import ch.wba.accounting.banana.BananaTransactionDto;

public class BananaValidator {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public Map<UUID, List<BananaViolation>> validate(final List<BananaTransactionDto> input) {
        return input.stream() //
            .map(this::validate) //
            .flatMap(Set<ConstraintViolation<BananaTransactionDto>>::stream) //
            .map(BananaViolation::new) //
            .collect(Collectors.groupingBy(BananaViolation::getUuid));
    }

    protected Set<ConstraintViolation<BananaTransactionDto>> validate(final BananaTransactionDto input) {
        final Set<ConstraintViolation<BananaTransactionDto>> violations = validator.validate(input);
        if (input.isComposedTransaction()) {
            final Set<ConstraintViolation<BananaTransactionDto>> integratedViolations = input.getIntegratedTransactions().stream() //
                .map(t -> validator.validate(t)) //
                .flatMap(Set<ConstraintViolation<BananaTransactionDto>>::stream) //
                .collect(Collectors.toSet());
            violations.addAll(integratedViolations);
        }
        if (input.isWithVat()) {
            violations.addAll(validator.validate(input, WithVatChecks.class));
        }
        return violations;
    }
}
