package ch.wba.accounting.banana;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.wba.accounting.converters.BigDecimalConverter;

public class BananaTransactionPostProcesserTest {
    private BananaTransactionPostProcesser testee;

    @Before
    public void setUp() {
        testee = new BananaTransactionPostProcesser();
    }

    @Test(expected = IllegalArgumentException.class)
    public void adjustTransactions_roundedVatWithoutMainTransaction_throwsException() {
        //arrange
        final BananaTransactionDto firstTransaction = createBananaDto("1", "First transaction");
        final BananaTransactionDto roundedTransaction = createBananaDto("2", "Rounded transaction");
        roundedTransaction.setVatCode(BananacConstants.VAT_UP_ROUNDED_CODE);
        final List<BananaTransactionDto> transactions = Arrays.asList(firstTransaction, roundedTransaction);
        //act
        testee.adjustTransactions(transactions);
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
        final List<BananaTransactionDto> result = testee.adjustTransactions(transactions);
        //assert
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getAmount(), is(BigDecimalConverter.toAmount("83.85")));
        assertThat(result.get(0).getAmountWithoutVat(), is(BigDecimalConverter.toAmount("77.85")));
        assertThat(result.get(0).getAmountVat(), is(BigDecimalConverter.toAmount("6.00")));
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
        final List<BananaTransactionDto> result = testee.adjustTransactions(transactions);
        //assert
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getAmount(), is(BigDecimalConverter.toAmount("220.75")));
        assertThat(result.get(0).getAmountWithoutVat(), is(BigDecimalConverter.toAmount("204.98")));
        assertThat(result.get(0).getAmountVat(), is(BigDecimalConverter.toAmount("15.77")));
    }

    @Test
    public void adjust_USTVatCode_changesUstVatCode() {
        //arrange
        final BananaTransactionDto transaction = createBananaDto("1", "Transaction");
        transaction.setVatCode("UST77");
        //act
        final List<BananaTransactionDto> result = testee.adjustTransactions(Arrays.asList(transaction));
        //assert
        assertThat(result.get(0).getVatCode(), is(BananacConstants.VAT_UST_77_CODE));
    }

    private BananaTransactionDto createBananaDto(final String document, final String description) {
        final BananaTransactionDto dto = new BananaTransactionDto();
        dto.setDocument(document);
        dto.setDescription(description);
        return dto;
    }
}
