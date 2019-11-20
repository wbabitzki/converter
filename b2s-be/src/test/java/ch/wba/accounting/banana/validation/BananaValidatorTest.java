package ch.wba.accounting.banana.validation;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
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
    private static final String DATE = "date";
    private static final String DOCUMENT = "document";
    private static final String DESCRIPTION = "description";
    private static final String FROM_ACCOUNT = "fromAccount";
    private static final String TO_ACCOUNT = "toAccount";
    private static final String AMOUNT = "amount";
    private static final String VAT_PCT = "vatPct";
    private static final String AMOUNT_WITHOUT_VAT = "amountWithoutVat";
    private static final String AMOUNT_VAT = "amountVat";
    private static final String VAT_ACCOUNT = "vatAccount";

    @Test
    public void validate_emptyRequiredFields_createsViolations() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertFalse(result.isEmpty());
        assertThat(result, Matchers.hasItem(hasViolation(DATE, testee.getUuid())));
        assertThat(result, Matchers.hasItem(hasViolation(DOCUMENT, testee.getUuid())));
        assertThat(result, Matchers.hasItem(hasViolation(DESCRIPTION, testee.getUuid())));
        assertThat(result, Matchers.hasItem(hasViolation(FROM_ACCOUNT, testee.getUuid())));
        assertThat(result, Matchers.hasItem(hasViolation(TO_ACCOUNT, testee.getUuid())));
    }

    @Test
    public void validate_emptyRequiredFieldsInComposedTransaction_createsViolations() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.addIntegratedTransaction(new BananaTransactionDto());
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertFalse(result.isEmpty());
        assertThat(result, Matchers.hasItem(hasViolation(DATE, testee.getUuid())));
        assertThat(result, Matchers.hasItem(hasViolation(DOCUMENT, testee.getUuid())));
        assertThat(result, Matchers.hasItem(hasViolation(DESCRIPTION, testee.getUuid())));
        assertThat(result, not(Matchers.hasItem(hasViolation(FROM_ACCOUNT, testee.getUuid()))));
        assertThat(result, Matchers.hasItem(hasViolation(TO_ACCOUNT, testee.getUuid())));
    }

    @Test
    public void validate_fromAccountInComposedTransaction_createsViolations() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.setDebitAccount("2000");
        testee.addIntegratedTransaction(new BananaTransactionDto());
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertFalse(result.isEmpty());
        assertThat(result, Matchers.hasItem(hasViolation(FROM_ACCOUNT, testee.getUuid())));
    }

    @Test
    public void validate_toAccountInComposedTransaction_noViolations() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.setCreditAccount("2000");
        testee.addIntegratedTransaction(new BananaTransactionDto());
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertFalse(result.isEmpty());
        assertThat(result, not(Matchers.hasItem(hasViolation(FROM_ACCOUNT, testee.getUuid()))));
        assertThat(result, not(Matchers.hasItem(hasViolation(TO_ACCOUNT, testee.getUuid()))));
    }

    @Test
    public void validate_emtpyRequiredFieldsInIntegratedTransaction_createsViolations() {
        //arrange
        final BananaTransactionDto integrated = new BananaTransactionDto();
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.addIntegratedTransaction(integrated);
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertFalse(result.isEmpty());
        assertThat(result, Matchers.hasItem(hasViolation(FROM_ACCOUNT, integrated.getUuid())));
        assertThat(result, not(Matchers.hasItem(hasViolation(TO_ACCOUNT, integrated.getUuid()))));
    }

    @Test
    public void validate_toAccountInIntegratedTransaction_createsViolations() {
        //arrange
        final BananaTransactionDto integrated = new BananaTransactionDto();
        integrated.setCreditAccount("2000");
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.addIntegratedTransaction(integrated);
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertFalse(result.isEmpty());
        assertThat(result, Matchers.hasItem(hasViolation(FROM_ACCOUNT, integrated.getUuid())));
        assertThat(result, Matchers.hasItem(hasViolation(TO_ACCOUNT, integrated.getUuid())));
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
        assertThat(result, Matchers.hasItem(hasViolation(VAT_PCT, testee.getUuid())));
        assertThat(result, Matchers.hasItem(hasViolation(AMOUNT_WITHOUT_VAT, testee.getUuid())));
        assertThat(result, Matchers.hasItem(hasViolation(AMOUNT_VAT, testee.getUuid())));
        assertThat(result, Matchers.hasItem(hasViolation(VAT_ACCOUNT, testee.getUuid())));
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
        assertThat(result, not(Matchers.hasItem(hasViolation(VAT_PCT, testee.getUuid()))));
        assertThat(result, not(Matchers.hasItem(hasViolation(AMOUNT_WITHOUT_VAT, testee.getUuid()))));
        assertThat(result, not(Matchers.hasItem(hasViolation(AMOUNT_VAT, testee.getUuid()))));
        assertThat(result, not(Matchers.hasItem(hasViolation(VAT_ACCOUNT, testee.getUuid()))));
    }

    @Test
    public void validate_transactions_createsViolations() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        //act
        final Map<UUID, List<BananaViolation>> result = new BananaValidator().validate(Collections.singletonList(testee));
        //assert
        assertFalse(result.isEmpty());
        assertThat(result.get(testee.getUuid()), Matchers.hasItem(hasViolation(DATE)));
        assertThat(result.get(testee.getUuid()), Matchers.hasItem(hasViolation(DOCUMENT)));
        assertThat(result.get(testee.getUuid()), Matchers.hasItem(hasViolation(AMOUNT)));
        assertThat(result.get(testee.getUuid()), Matchers.hasItem(hasViolation(DESCRIPTION)));
        assertThat(result.get(testee.getUuid()), Matchers.hasItem(hasViolation(TO_ACCOUNT)));
        assertThat(result.get(testee.getUuid()), Matchers.hasItem(hasViolation(FROM_ACCOUNT)));
    }

    @Test
    public void validate_integratedTransactions_createsViolations() {
        //arrange
        final BananaTransactionDto main = new BananaTransactionDto();
        final BananaTransactionDto integrated = new BananaTransactionDto();
        main.addIntegratedTransaction(integrated);
        //act
        final Map<UUID, List<BananaViolation>> result = new BananaValidator().validate(Collections.singletonList(main));
        //assert
        assertFalse(result.isEmpty());
        assertThat(result.get(main.getUuid()), Matchers.hasItem(hasViolation(DATE)));
        assertThat(result.get(main.getUuid()), Matchers.hasItem(hasViolation(DOCUMENT)));
        assertThat(result.get(main.getUuid()), Matchers.hasItem(hasViolation(DESCRIPTION)));
        assertThat(result.get(main.getUuid()), Matchers.hasItem(hasViolation(TO_ACCOUNT)));
        assertThat(result.get(main.getUuid()), Matchers.hasItem(not(hasViolation(FROM_ACCOUNT))));
        assertThat(result.get(integrated.getUuid()), Matchers.hasItem(hasViolation(DATE)));
        assertThat(result.get(integrated.getUuid()), Matchers.hasItem(hasViolation(DOCUMENT)));
        assertThat(result.get(integrated.getUuid()), Matchers.hasItem(hasViolation(DESCRIPTION)));
        assertThat(result.get(integrated.getUuid()), Matchers.hasItem(not(hasViolation(TO_ACCOUNT))));
        assertThat(result.get(integrated.getUuid()), Matchers.hasItem(hasViolation(FROM_ACCOUNT)));
    }

    @Test
    public void validate_amountWithoutVat_noViolations() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.setDate(LocalDate.now());
        testee.setDocument("38");
        testee.setDescription("Test Description Amount without VAT");
        testee.setDebitAccount("1111");
        testee.setCreditAccount("2222");
        testee.setAmount(new BigDecimal("2500"));
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void validate_notComposedTransaction_noComposedTransactionSumViolation() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.setAmount(new BigDecimal("1500"));
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertThat(result, not(Matchers.hasItem(hasViolation(ComposedTransactionSumValidator.FIELD_AMOUNT, testee.getUuid()))));
    }

    @Test
    public void validate_invalidSumInComposedTransaction_createsViolation() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.setAmount(new BigDecimal("1500"));
        testee.addIntegratedTransaction(new BananaTransactionDto());
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertThat(result, Matchers.hasItem(hasViolation(ComposedTransactionSumValidator.FIELD_AMOUNT, //
                "Invalid sum of the composed transaction. The amount of the main transaction: 1'500.00, but the sum of integrated transaction: 0.00", //
                testee.getUuid())));
    }

    @Test
    public void validate_correctSumInComposedTransaction_createsViolation() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.setAmount(new BigDecimal("1500"));
        BananaTransactionDto integrated = new BananaTransactionDto();
        integrated.setAmount(new BigDecimal("1500"));
        testee.addIntegratedTransaction(integrated);
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertThat(result, not(Matchers.hasItem(hasViolation(ComposedTransactionSumValidator.FIELD_AMOUNT, testee.getUuid()))));
    }

    @Test
    public void validate_notComposedTransaction_noIntegratedTransactionDateViolation() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.setDate(LocalDate.now());
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertThat(result, not(Matchers.hasItem(hasViolation(IntegratedTransactionDateValidator.FIELD_DATE, testee.getUuid()))));
    }

    @Test
    public void validate_invalidDateInIntegratedTransaction_createsViolation() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.setDate(LocalDate.of(2019, 11, 20));
        final BananaTransactionDto composeTransaction = new BananaTransactionDto();
        composeTransaction.setDate(LocalDate.of(2019, 11, 21));
        composeTransaction.addIntegratedTransaction(testee);
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertThat(result, Matchers.hasItem(hasViolation(IntegratedTransactionDateValidator.FIELD_DATE, //
                "Invalid bill date '20.11.2019' in the integrated transaction. Date in the main transaction: 21.11.2019", //
                testee.getUuid())));
    }

    @Test
    public void validate_validDateInIntegratedTransaction_noIntegratedTransactionDateViolation() {
        //arrange
        final BananaTransactionDto testee = new BananaTransactionDto();
        testee.setDate(LocalDate.now());
        final BananaTransactionDto composeTransaction = new BananaTransactionDto();
        composeTransaction.setDate(LocalDate.now());
        composeTransaction.addIntegratedTransaction(testee);
        //act
        final Set<ConstraintViolation<BananaTransactionDto>> result = new BananaValidator().validate(testee);
        //assert
        assertThat(result, not(Matchers.hasItem(hasViolation(IntegratedTransactionDateValidator.FIELD_DATE, testee.getUuid()))));
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

    private Matcher<ConstraintViolation<BananaTransactionDto>> hasViolation(final String field, final String message, final UUID uuid) {
        return Matchers.allOf( //
                Matchers.hasProperty("propertyPath", Matchers.hasToString(field)), //
                Matchers.hasProperty("rootBean", Matchers.hasProperty("uuid", is(uuid))), //
                Matchers.hasProperty("messageTemplate", is(message)) //
        );
    }
}
