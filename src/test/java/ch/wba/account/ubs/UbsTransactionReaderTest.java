package ch.wba.account.ubs;

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
        testee.read(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void read_emptyCsvFile_throwsExcetption() throws Exception {
        //arrange
        final UbsTransactionReader testee = new UbsTransactionReader();
        final InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_EMPTY_FILE);
        //act
        testee.read(is);
    }

    @Test(expected = IllegalArgumentException.class)
    public void read_noHeader_throwsExcetption() throws Exception {
        //arrange
        final UbsTransactionReader testee = new UbsTransactionReader();
        final InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_WITHOUT_HEADER);
        //act
        testee.read(is);
    }

    @Test(expected = IllegalArgumentException.class)
    public void read_noFooter_throwsExcetption() throws Exception {
        //arrange
        final UbsTransactionReader testee = new UbsTransactionReader();
        final InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_WITHOUTFOOTER_CSV);
        //act
        testee.read(is);
    }

    @Test
    public void read_validFile_returnsParsedValues() throws Exception {
        //arrange
        final UbsTransactionReader testee = new UbsTransactionReader();
        final InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_VALID_FILE);
        final UbsTransactionDto expacted = new UbsTransactionDto();
        expacted.setValuationDate("29.09.2017");
        expacted.setBankingRelationship("0235 00547895");
        expacted.setProduct("0235 00547895.01H");
        expacted.setIban("CH69 0023 5235 5478 9501 H");
        expacted.setCcy("CHF");
        expacted.setFromDate("01.07.2017");
        expacted.setToDate("30.09.2017");
        expacted.setDescription("UBS Business Current Account");
        expacted.setTradeDate("04.07.2017");
        expacted.setBookingDate("04.07.2017");
        expacted.setValueDate("04.07.2017");
        expacted.setDescription1("e-banking Order");
        expacted.setDescription2("swisscom");
        expacted.setDescription3("Wladimir Babitzki, 6206 Neuenkirch, E-Banking booktransfer");
        expacted.setTransactionNo("9935185TI2774507");
        expacted.setDebit("179.00");
        expacted.setBalance("42'449.82");

        //act
        final List<UbsTransactionDto> lines = testee.read(is);

        //assert
        assertThat(lines, hasSize(90));
        assertThat(lines.get(0), is(expacted));
    }

    @Test
    public void read_missingFields_missingFieldsAreEmpty() throws Exception {
        //arrange
        final UbsTransactionReader testee = new UbsTransactionReader();
        final InputStream is = getClass().getClassLoader().getResourceAsStream(TEST_MISSING_FIELDS_CSV);
        final UbsTransactionDto expacted = new UbsTransactionDto();
        expacted.setValuationDate("29.09.2017");
        expacted.setBankingRelationship("0235 00547895");
        expacted.setProduct("0235 00547895.01H");
        expacted.setIban("CH69 0023 5235 5478 9501 H");
        expacted.setCcy("CHF");
        expacted.setFromDate("01.07.2017");
        expacted.setToDate("30.09.2017");
        expacted.setDescription("UBS Business Current Account");
        expacted.setTradeDate("06.07.2017");
        expacted.setBookingDate("06.07.2017");
        expacted.setValueDate("06.07.2017");
        expacted.setDescription1("E-Banking CHF domestic");
        expacted.setTransactionNo("9935186TI3104156");
        //act
        final List<UbsTransactionDto> lines = testee.read(is);
        //assert
        assertThat(lines.get(0), is(expacted));

    }

}