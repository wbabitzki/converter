package ch.wba.accounting.banana.validation;

import java.util.UUID;

import javax.validation.ConstraintViolation;

import ch.wba.accounting.banana.BananaTransactionDto;

public class BananaViolation {
    private final UUID uuid;
    private final String field;
    private final String message;

    public BananaViolation(final UUID uuid, final String field, final String message) {
        this.uuid = uuid;
        this.field = field;
        this.message = message;
    }

    public BananaViolation(final ConstraintViolation<BananaTransactionDto> violation) {
        final BananaTransactionDto dto = violation.getRootBean();
        uuid = dto.getUuid();
        field = violation.getPropertyPath().toString();
        message = violation.getMessage();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
