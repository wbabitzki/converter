package ch.wba.accounting.ubs;

import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

public class UbsTransactionReaderTest {

    private static final String TEST_EMPTY_FILE = "test-emptyFile.csv";
    private static final String TEST_WITHOUT_HEADER = "test-withoutHeader.csv";
    private static final String TEST_VALID_FILE = "test-validFile.csv";
    private static final String TEST_MISSING_FIELDS_CSV = "test-missingFields.csv";
    private static final String TEST_WITHOUTFOOTER_CSV = "test-withoutFooter.csv";


    @Test(expected = IllegalArgumentException.class)
    public void read_null_throwsException() throws Exception {
        //arrange
        final UbsTransactionReader testee = new UbsTransactionReader();
        //act
        testee.readTransactions(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void read_emptyCsvFile_throwsExcetption() throws Exception {
        //arrange
        final UbsTransactionReader testee = new UbsTransactionReader();
        final InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_EMPTY_FILE);
        //act
        testee.readTransactions(is);
    }

    @Test(expected = IllegalArgumentException.class)
    public void read_noHeader_throwsExcetption() throws Exception {
        //arrange
        final UbsTransactionReader testee = new UbsTransactionReader();
        final InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_WITHOUT_HEADER);
        //act
        testee.readTransactions(is);
    }

    @Test(expected = IllegalArgumentException.class)
    public void read_noFooter_throwsExcetption() throws Exception {
        //arrange
        final UbsTransactionReader testee = new UbsTransactionReader();
        final InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_WITHOUTFOOTER_CSV);
        //act
        testee.readTransactions(is);
    }

    @Test
    public void read_validFile_returnsParsedValues() throws Exception {
        //arrange
        final UbsTransactionReader testee = new UbsTransactionReader();
        final InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_VALID_FILE);
        final UbsTransactionDto expected = new UbsTransactionDto();
        expected.setValuationDate("29.09.2017");
        expected.setBankingRelationship("0235 00547895");
        expected.setProduct("0235 00547895.01H");
        expected.setIban("CH69 0023 5235 5478 9501 H");
        expected.setCcy("CHF");
        expected.setFromDate("01.07.2017");
        expected.setToDate("30.09.2017");
        expected.setDescription("UBS Business Current Account");
        expected.setTradeDate("04.07.2017");
        expected.setBookingDate("04.07.2017");
        expected.setValueDate("04.07.2017");
        expected.setDescription1("e-banking Order");
        expected.setDescription2("swisscom");
        expected.setDescription3("Wladimir Babitzki, 6206 Neuenkirch, E-Banking booktransfer");
        expected.setTransactionNo("9935185TI2774507");
        expected.setDebit("179.00");
        expected.setBalance("42'449.82");

        //act
        final List<UbsTransactionDto> transactions = testee.readTransactions(is);

        //assert
        assertThat(transactions, hasSize(90));
        assertThat(transactions.get(0), is(expected));
    }

    @Test
    public void read_validFile_returnsBalance() throws Exception {
        //arrange
        final UbsTransactionReader testee = new UbsTransactionReader();
        final InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_VALID_FILE);
        final UbsBalanceDto expected = new UbsBalanceDto();
        expected.setClosingBalance("59066.71");
        expected.setOpeningBalance("42628.82");

        //act
        final UbsBalanceDto balances = testee.readBalance(is);

        //assert
        assertThat(balances, is(expected));
    }

    @Test
    public void read_missingFields_missingFieldsAreEmpty() throws Exception {
        //arrange
        final UbsTransactionReader testee = new UbsTransactionReader();
        final InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_MISSING_FIELDS_CSV);
        final UbsTransactionDto expected = new UbsTransactionDto();
        expected.setValuationDate("29.09.2017");
        expected.setBankingRelationship("0235 00547895");
        expected.setProduct("0235 00547895.01H");
        expected.setIban("CH69 0023 5235 5478 9501 H");
        expected.setCcy("CHF");
        expected.setFromDate("01.07.2017");
        expected.setToDate("30.09.2017");
        expected.setDescription("UBS Business Current Account");
        expected.setTradeDate("06.07.2017");
        expected.setBookingDate("06.07.2017");
        expected.setValueDate("06.07.2017");
        expected.setDescription1("E-Banking CHF domestic");
        expected.setTransactionNo("9935186TI3104156");
        //act
        final List<UbsTransactionDto> lines = testee.readTransactions(is);
        //assert
        assertThat(lines.get(0), is(expected));
    }

}