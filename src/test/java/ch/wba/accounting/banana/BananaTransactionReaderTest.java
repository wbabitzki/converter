package ch.wba.accounting.banana;

import ch.wba.accounting.converters.BigDecimalConverter;
import ch.wba.accounting.converters.LocalDateConverter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;

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
        BananaTransactionDto transaction = BananaTransactionReader.MAPPER.apply("");
        //assert
        assertNull(transaction);
    }

    @Test
    public void map_notDateFirst_returnsNull() {
        //act
        BananaTransactionDto transaction = BananaTransactionReader.MAPPER.apply("foo,boo,qoo,doo");
        //assert
        assertNull(transaction);
    }

    @Test
    public void map_validInputString_returnsValidDto() {
        //arrange
        final String input = "05.01.2018,1,Zahlungseingang Rechnung 103,1020,3000,15'000.00,B80,,-8,-8,13'888.89,-1'111.11,2201,,,-1'111.11,";
        //act
        final BananaTransactionDto transaction = BananaTransactionReader.MAPPER.apply(input);
        //assert
        assertNotNull(transaction);
        assertThat(transaction.getDate(), is(LocalDateConverter.toDate("05.01.2018")));
        assertThat(transaction.getDocument(), is("1"));
        assertThat(transaction.getDescription(), is("Zahlungseingang Rechnung 103"));
        assertThat(transaction.getDebitAccount(), is("1020"));
        assertThat(transaction.getCreditAccount(), is("3000"));
        assertThat(transaction.getAmount(), is(BigDecimalConverter.toAmount("15000.00")));
        assertThat(transaction.getVatCode(), is("B80"));
        assertThat(transaction.getVatPct(), is(BigDecimalConverter.toAmount("-8.00")));
        assertThat(transaction.getAmountWithoutVat(), is(BigDecimalConverter.toAmount("13'888.89")));
        assertThat(transaction.getAmountVat(), is(BigDecimalConverter.toAmount("-1'111.11")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void read_null_throwsException() throws Exception {
        //act
        testee.readTransactions(null);
    }

    @Test
    public void map_emptyOptionalValues_valuesAreNull() throws IOException {
        //arrange
        final String input = "05.01.2018,2,Bareinzahlung Kasse,1000,1020,2'000.00,,,,,,,,,,,";
        //act
        final BananaTransactionDto transaction = BananaTransactionReader.MAPPER.apply(input);
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
    public void readTransactions_validFile_expectedLineCount() throws IOException {
        //arrange
        final InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_VALID_FILE);
        //act
        final List<BananaTransactionDto> transactions = testee.readTransactions(is);
        //assert
        assertThat(transactions, hasSize(17));
    }
}