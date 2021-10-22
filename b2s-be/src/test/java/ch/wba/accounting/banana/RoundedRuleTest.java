package ch.wba.accounting.banana;

import ch.wba.accounting.converters.BigDecimalConverter;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RoundedRuleTest {
    private RoundedRule testee;

    @Before
    public void setUp() {
        testee = new RoundedRule();
    }

    @Test
    public void applicable_withoutRoundedTransaction_false() {
        // arrange
        final BananaTransactionDto roundedTransaction = createBananaDto("1", "Rounded transaction");
        // act
        final boolean result = testee.applicable(roundedTransaction);
        // assert
        assertThat(result, is(false));
    }

    @Test
    public void applicable_withRoundedTransaction_true() {
        // arrange
        final BananaTransactionDto roundedTransaction = createBananaDto("1", "Rounded transaction");
        roundedTransaction.setVatCode(BananacConstants.VAT_UP_ROUNDED_CODE);
        // act
        final boolean result = testee.applicable(roundedTransaction);
        // assert
        assertThat(result, is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void adjustTransactions_roundedVatWithoutMainTransaction_throwsException() {
        // arrange
        final BananaTransactionDto firstTransaction = createBananaDto("1", "First transaction");
        final BananaTransactionDto roundedTransaction = createBananaDto("2", "Rounded transaction");
        roundedTransaction.setVatCode(BananacConstants.VAT_UP_ROUNDED_CODE);
        final List<BananaTransactionDto> transactions = Arrays.asList(firstTransaction, roundedTransaction);
        // act
        testee.apply(1, transactions);
    }

    @Test
    public void adjustTransactions_upRroundedVatWithMainTransaction_adjustedToRoundedAmount() {
        //arrange
        final BananaTransactionDto firstTransaction = createBananaDto("1", "First transaction");
        firstTransaction.setAmount(BigDecimalConverter.toAmount("83.85"));
        firstTransaction.setAmountVat(BigDecimalConverter.toAmount("5.99"));
        firstTransaction.setAmountWithoutVat(BigDecimalConverter.toAmount("77.86"));
        final BananaTransactionDto roundedTransaction = createBananaDto("1", "Rounded transaction");
        roundedTransaction.setVatCode(BananacConstants.VAT_UP_ROUNDED_CODE);
        roundedTransaction.setAmountVat(BigDecimalConverter.toAmount("0.01"));
        final List<BananaTransactionDto> transactions = Arrays.asList(firstTransaction, roundedTransaction);
        //act
        final List<BananaTransactionDto> result = testee.apply(1, transactions);
        //assert
        assertThat(result.size(), CoreMatchers.is(2));
        assertThat(result.get(0).getAmount(), CoreMatchers.is(BigDecimalConverter.toAmount("83.85")));
        assertThat(result.get(0).getAmountWithoutVat(), CoreMatchers.is(BigDecimalConverter.toAmount("77.85")));
        assertThat(result.get(0).getAmountVat(), CoreMatchers.is(BigDecimalConverter.toAmount("6.00")));
    }

    @Test
    public void adjustTransactions_offRoundedVatWithMainTransaction_adjustedToRoundedAmount() {
        //arrange
        final BananaTransactionDto firstTransaction = createBananaDto("1", "First transaction");
        firstTransaction.setAmount(BigDecimalConverter.toAmount("220.75"));
        firstTransaction.setAmountVat(BigDecimalConverter.toAmount("15.78"));
        firstTransaction.setAmountWithoutVat(BigDecimalConverter.toAmount("204.97"));
        final BananaTransactionDto roundedTransaction = createBananaDto("1", "Rounded transaction");
        roundedTransaction.setVatCode(BananacConstants.VAT_OFF_ROUNDED_CODE);
        roundedTransaction.setAmountVat(BigDecimalConverter.toAmount("-0.01"));
        final List<BananaTransactionDto> transactions = Arrays.asList(firstTransaction, roundedTransaction);
        //act
        final List<BananaTransactionDto> result = testee.apply(1, transactions);;
        //assert
        assertThat(result.size(), CoreMatchers.is(2));
        assertThat(result.get(0).getAmount(), CoreMatchers.is(BigDecimalConverter.toAmount("220.75")));
        assertThat(result.get(0).getAmountWithoutVat(), CoreMatchers.is(BigDecimalConverter.toAmount("204.98")));
        assertThat(result.get(0).getAmountVat(), CoreMatchers.is(BigDecimalConverter.toAmount("15.77")));
    }

    private BananaTransactionDto createBananaDto(final String document, final String description) {
        final BananaTransactionDto dto = new BananaTransactionDto();
        dto.setDocument(document);
        dto.setDescription(description);
        return dto;
    }

}