package ch.wba.accounting.banana.validation;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import ch.wba.accounting.banana.BananaTransactionDto;

public class BananaValidatorTest {
    @SuppressWarnings("unchecked")
    @Test
    public void validate_emtpyRequiredFields_createsViolations() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertFalse(result.isEmpty());
        assertThat(result, Matchers.hasItems( //
            hasViolation("date", testee.getUuid()), //
            hasViolation("document", testee.getUuid()), //
            hasViolation("description", testee.getUuid()), //
            hasViolation("fromAccount", testee.getUuid()), //
            hasViolation("toAccount", testee.getUuid()) //
        ));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void validate_emtpyRequiredFieldsInComposedTransation_createsViolations() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.addComposedTransaction(new BananaTransactionDto());
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertFalse(result.isEmpty());
        assertThat(result, Matchers.hasItems( //
            hasViolation("date", testee.getUuid()), //
            hasViolation("document", testee.getUuid()), //
            hasViolation("description", testee.getUuid()), //
            not(hasViolation("fromAccount", testee.getUuid())), //
            hasViolation("toAccount", testee.getUuid()) //
        ));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void validate_fromAccountInComposedTransation_createsViolations() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.setDebitAccount("2000");
        testee.addComposedTransaction(new BananaTransactionDto());
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertFalse(result.isEmpty());
        assertThat(result, Matchers.hasItems( //
            hasViolation("fromAccount", testee.getUuid()) //
        ));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void validate_emtpyRequiredFieldsInIntegratedTransation_createsViolations() {
        //arrange
        final BananaTransactionDto integrated = new BananaTransactionDto();
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.addComposedTransaction(integrated);
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertFalse(result.isEmpty());
        assertThat(result, Matchers.hasItems( //
            not(hasViolation("toAccount", integrated.getUuid())), //
            hasViolation("fromAccount", integrated.getUuid()) //
        ));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void validate_toAccountInIntegratedTransation_createsViolations() {
        //arrange
        final BananaTransactionDto integrated = new BananaTransactionDto();
        integrated.setCreditAccount("2000");
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.addComposedTransaction(integrated);
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertFalse(result.isEmpty());
        assertThat(result, Matchers.hasItems( //
            not(hasViolation("fromAccount", integrated.getUuid())), //
            hasViolation("toAccount", integrated.getUuid()) //
        ));
    }

    @Test
    public void validate_vatNotComplete_createsViolations() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.setVatCode("TST");
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertFalse(result.isEmpty());
        assertTrue(hasViolationAtField(result, "vatPct"));
        assertTrue(hasViolationAtField(result, "amountWithoutVat"));
        assertTrue(hasViolationAtField(result, "amountVat"));
        assertTrue(hasViolationAtField(result, "vatAccount"));
    }

    @Test
    public void validate_vatComplete_noVatViolations() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.setVatCode("TST");
        testee.setVatAccount("7777");
        testee.setVatPct(new BigDecimal("10"));
        testee.setAmountVat(new BigDecimal("10"));
        testee.setAmountWithoutVat(new BigDecimal("90"));
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertFalse(hasViolationAtField(result, "vatPct"));
        assertFalse(hasViolationAtField(result, "amountWithoutVat"));
        assertFalse(hasViolationAtField(result, "amountVat"));
        assertFalse(hasViolationAtField(result, "vatAccount"));
    }

    private boolean hasViolationAtField(final Set<ConstraintViolation<BananaTransactionDto>> violations, final String field) {
        return violations.stream().anyMatch(constraint -> constraint.getPropertyPath().toString().equals(field));
    }

    @Test
    public void validate_transactions_createsViolations() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        //act
        final Map<UUID, List<BananaViolation>> result = new BananaValidator().validate(Arrays.asList(testee));
        //assert
        assertFalse(result.isEmpty());
        assertThat(result.get(testee.getUuid()), Matchers.hasItem(hasViolation("date")));
        assertThat(result.get(testee.getUuid()), Matchers.hasItem(hasViolation("document")));
        assertThat(result.get(testee.getUuid()), Matchers.hasItem(hasViolation("amount")));
        assertThat(result.get(testee.getUuid()), Matchers.hasItem(hasViolation("description")));
        assertThat(result.get(testee.getUuid()), Matchers.hasItem(hasViolation("toAccount")));
        assertThat(result.get(testee.getUuid()), Matchers.hasItem(hasViolation("fromAccount")));
    }

    @Test
    public void validate_integratedTransactions_createsViolations() {
        //arrange
        final BananaTransactionDto main = new BananaTransactionDto();
        final BananaTransactionDto integrated = new BananaTransactionDto();
        main.addComposedTransaction(integrated);
        //act
        final Map<UUID, List<BananaViolation>> result = new BananaValidator().validate(Arrays.asList(main));
        //assert
        assertFalse(result.isEmpty());
        assertThat(result.get(main.getUuid()), Matchers.hasItem(hasViolation("date")));
        assertThat(result.get(main.getUuid()), Matchers.hasItem(hasViolation("document")));
        assertThat(result.get(main.getUuid()), Matchers.hasItem(hasViolation("description")));
        assertThat(result.get(main.getUuid()), Matchers.hasItem(hasViolation("toAccount")));
        assertThat(result.get(main.getUuid()), Matchers.hasItem(not(hasViolation("fromAccount"))));
        assertThat(result.get(integrated.getUuid()), Matchers.hasItem(hasViolation("date")));
        assertThat(result.get(integrated.getUuid()), Matchers.hasItem(hasViolation("document")));
        assertThat(result.get(integrated.getUuid()), Matchers.hasItem(hasViolation("description")));
        assertThat(result.get(integrated.getUuid()), Matchers.hasItem(not(hasViolation("toAccount"))));
        assertThat(result.get(integrated.getUuid()), Matchers.hasItem(hasViolation("fromAccount")));
    }

    private Matcher<Object> hasViolation(final String field) {
        return Matchers.hasProperty("field", is(field));
    }

    private Matcher<ConstraintViolation<BananaTransactionDto>> hasViolation(final String field, final UUID uuid) {
        return Matchers.allOf( //
            Matchers.hasProperty("propertyPath", Matchers.hasToString(field)), //
            Matchers.hasProperty("rootBean", Matchers.hasProperty("uuid", is(uuid))) //
        );
    }
}
