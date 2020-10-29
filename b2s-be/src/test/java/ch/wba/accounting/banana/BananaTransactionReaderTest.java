package ch.wba.accounting.banana;

import ch.wba.accounting.banana.exception.BananaReaderExceptions;
import ch.wba.accounting.converters.BigDecimalConverter;
import ch.wba.accounting.converters.LocalDateConverter;
import com.opencsv.CSVReader;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BananaTransactionReaderTest {
    private static final String TEST_VALID_FILE = "test-banana.csv";
    private static final String TEST_VALID_TAB_FILE = "test-banana-tab.csv";

    private BananaTransactionReader testee;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        testee = new BananaTransactionReader();
    }

    @Test
    public void map_validInputString_returnsValidDto() {
        //arrange
        final String input = "05.01.2018,1,Zahlungseingang Rechnung 103,1020,3000,15'000.00,B80,,-8,-8,13'888.89,-1'111.11,2201,,,-1'111.11,";
        //act
        final BananaTransactionDto transaction = testee.toBananaDto(input, ',');
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
        final BananaTransactionDto transaction = testee.toBananaDto(input, ',');
        //assert
        assertNotNull(transaction);
        assertThat(transaction.getDate(), is(LocalDateConverter.toDate("05.01.2018")));
        assertThat(transaction.getDocument(), is("2"));
        assertThat(transaction.getDescription(), is("Bareinzahlung Kasse"));
        assertThat(transaction.getDebitAccount(), is("1000"));
        assertThat(transaction.getCreditAccount(), is("1020"));
        assertThat(transaction.getAmount(), is(BigDecimalConverter.toAmount("2000.00")));
        assertThat(transaction.getVatCode(), is(""));
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
        final List<BananaTransactionDto> transactions = testee.readTransactions(reader, ',');
        //assert
        assertThat(transactions.size(), is(11));
    }

    @Test
    public void readTransactions_validTabSeparatedFile_expectedLineCount() {
        //arrange
        final InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_VALID_TAB_FILE);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        //act
        final List<BananaTransactionDto> transactions = testee.readTransactions(reader, '\t');
        //assert
        assertThat(transactions.size(), is(23));
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
        assertThat(result.get(1).getIntegratedTransactions().get(0), is(secondComposed));
        assertTrue(result.get(1).isComposedTransaction());
        assertThat(result.get(2), is(secondSimple));
        assertFalse(result.get(2).isComposedTransaction());
    }

    @Test
    public void toBananaDto_readerThrowsException_InvalidLineException() throws Exception {
        //arrange & assert
        final CSVReader csvReader = mock(CSVReader.class);
        when(csvReader.readNext()).thenThrow(IOException.class);
        BananaTransactionReader reader = new BananaTransactionReader() {
            @Override
            protected CSVReader createCsvReader(String line, char delimiter) {
                return csvReader;
            }
        };
        exceptionRule.expect(BananaReaderExceptions.InvalidLineException.class);
        exceptionRule.expectMessage("Invalid line: \"Test\"");
        //act
        reader.toBananaDto("Test", ',');
    }

    @Test
    public void toBananaDto_invalidDate_InvalidDateException() {
        //arrange & assert
        final String input = "05012018,1,Zahlungseingang Rechnung 103,1020,3000,15'000.00,B80,,-8,-8,13'888.89,-1'111.11,2201,,,-1'111.11,";
        exceptionRule.expect(BananaReaderExceptions.InvalidDateException.class);
        exceptionRule.expectMessage(String.format("Invalid date: \"%s\" in line \"%s\"", "05012018", input));
        //act
        testee.toBananaDto(input, ',');
    }

    @Test
    public void toBananaDto_invalidFieldNumber_InvalidFieldNumberException() {
        //arrange & assert
        final String input = "1,Zahlungseingang Rechnung 103,1020,3000,15'000.00,B80,,-8,-8,13'888.89,-1'111.11,2201,,,-1'111.11,";
        exceptionRule.expect(BananaReaderExceptions.InvalidFieldNumberException.class);
        exceptionRule.expectMessage(String.format("Invalid fields number. Expected %d but was %d", 17, 16));
        //act
        testee.toBananaDto(input, ',');
    }
    private BananaTransactionDto createBananaDto(final String document, final String description) {
        final BananaTransactionDto firstSimple = new BananaTransactionDto();
        firstSimple.setDocument(document);
        firstSimple.setDescription(description);
        return firstSimple;
    }
}