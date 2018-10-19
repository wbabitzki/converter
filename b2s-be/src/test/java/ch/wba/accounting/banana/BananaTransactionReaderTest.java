package ch.wba.accounting.banana;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.wba.accounting.converters.BigDecimalConverter;
import ch.wba.accounting.converters.LocalDateConverter;

public class BananaTransactionReaderTest {
    private static final String TEST_VALID_FILE = "test-banana.csv";
    private BananaTransactionReader testee;

    @Before
    public void setUp() {
        testee = new BananaTransactionReader();
    }

    @Test
    public void map_emptyString_returnsNull() {
        //act
        final BananaTransactionDto transaction = BananaTransactionReader.STRING_TO_DTO_MAPPER.apply("");
        //assert
        assertNull(transaction);
    }

    @Test
    public void map_notDateFirst_returnsNull() {
        //act
        final BananaTransactionDto transaction = BananaTransactionReader.STRING_TO_DTO_MAPPER.apply("foo,boo,qoo,doo");
        //assert
        assertNull(transaction);
    }

    @Test
    public void map_validInputString_returnsValidDto() {
        //arrange
        final String input = "05.01.2018,1,Zahlungseingang Rechnung 103,1020,3000,15'000.00,B80,,-8,-8,13'888.89,-1'111.11,2201,,,-1'111.11,";
        //act
        final BananaTransactionDto transaction = BananaTransactionReader.STRING_TO_DTO_MAPPER.apply(input);
        //assert
        assertNotNull(transaction);
        assertThat(transaction.getDate(), is(LocalDateConverter.toDate("05.01.2018")));
        assertThat(transaction.getDocument(), is("1"));
        assertThat(transaction.getDescription(), is("Zahlungseingang Rechnung 103"));
        assertThat(transaction.getDebitAccount(), is("1020"));
        assertThat(transaction.getCreditAccount(), is("3000"));
        assertThat(transaction.getAmount(), is(BigDecimalConverter.toAmount("15000.00")));
        assertThat(transaction.getVatCode(), is("B80"));
        assertThat(transaction.getVatPct(), is(BigDecimalConverter.toPct("-8.0")));
        assertThat(transaction.getAmountWithoutVat(), is(BigDecimalConverter.toAmount("13'888.89")));
        assertThat(transaction.getAmountVat(), is(BigDecimalConverter.toAmount("-1'111.11")));
        assertThat(transaction.getVatAccount(), is("2201"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void read_null_throwsException() {
        //act
        testee.readTransactions(null);
    }

    @Test
    public void map_emptyOptionalValues_valuesAreNull() {
        //arrange
        final String input = "05.01.2018,2,Bareinzahlung Kasse,1000,1020,2'000.00,,,,,,,,,,,";
        //act
        final BananaTransactionDto transaction = BananaTransactionReader.STRING_TO_DTO_MAPPER.apply(input);
        //assert
        assertNotNull(transaction);
        assertThat(transaction.getDate(), is(LocalDateConverter.toDate("05.01.2018")));
        assertThat(transaction.getDocument(), is("2"));
        assertThat(transaction.getDescription(), is("Bareinzahlung Kasse"));
        assertThat(transaction.getDebitAccount(), is("1000"));
        assertThat(transaction.getCreditAccount(), is("1020"));
        assertThat(transaction.getAmount(), is(BigDecimalConverter.toAmount("2000.00")));
        assertNull(transaction.getVatCode());
        assertNull(transaction.getVatPct());
        assertNull(transaction.getAmountWithoutVat());
        assertNull(transaction.getAmountVat());
    }

    @Test
    public void readTransactions_validFile_expectedLineCount() {
        //arrange
        final InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_VALID_FILE);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        //act
        final List<BananaTransactionDto> transactions = testee.readTransactions(reader);
        //assert
        assertThat(transactions.size(), is(11));
    }

    @Test
    public void composedTransactionsCollector_mixedBananaDtos_returnsComposedDtos() {
        //arrange
        final BananaTransactionDto firstSimple = createBananaDto("1", "First simple");
        final BananaTransactionDto firstComposed = createBananaDto("2", "First composed");
        final BananaTransactionDto secondComposed = createBananaDto("2", "Second composed");
        final BananaTransactionDto secondSimple = createBananaDto("3", "Second simple");
        final List<BananaTransactionDto> input = Arrays.asList(firstSimple, firstComposed, secondComposed, secondSimple);
        //act
        final List<BananaTransactionDto> result = new ArrayList<>(input.stream().collect(BananaTransactionReader.COMPOSED_TRANSACTION_COLLECTOR).values());
        //assert
        assertThat(result.size(), is(3));
        assertThat(result.get(0), is(firstSimple));
        assertFalse(result.get(0).isComposedTransaction());
        assertThat(result.get(1), is(firstComposed));
        assertThat(result.get(1).getComposedTransactions().get(0), is(secondComposed));
        assertTrue(result.get(1).isComposedTransaction());
        assertThat(result.get(2), is(secondSimple));
        assertFalse(result.get(2).isComposedTransaction());
    }

    @Test(expected = IllegalArgumentException.class)
    public void adjustTransactions_roundedVatWithoutMainTransaction_throwsException() {
        //arrange
        final BananaTransactionDto firstTransaction = createBananaDto("1", "First transaction");
        final BananaTransactionDto roundedTransaction = createBananaDto("2", "Rounded transaction");
        roundedTransaction.setVatCode(BananaTransactionReader.VAT_UP_ROUNDED_CODE);
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
        roundedTransaction.setVatCode(BananaTransactionReader.VAT_UP_ROUNDED_CODE);
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
        roundedTransaction.setVatCode(BananaTransactionReader.VAT_OFF_ROUNDED_CODE);
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
        assertThat(result.get(0).getVatCode(), is(BananaTransactionReader.VAT_UST_77_CODE));
    }

    private BananaTransactionDto createBananaDto(final String document, final String description) {
        final BananaTransactionDto firstSimple = new BananaTransactionDto();
        firstSimple.setDocument(document);
        firstSimple.setDescription(description);
        return firstSimple;
    }
}