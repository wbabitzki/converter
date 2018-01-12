package ch.wba.accounting;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class AccountTransactionWriterTest {

    private static final String TEST_THREE_EMPTY_LINES = ";;;;;;;\n;;;;;;;\n;;;;;;;\n";

    @Test(expected = IllegalArgumentException.class)
    public void constructor_streamIsNull_throwsException() {
        AccountTransactionWriter testee = new AccountTransactionWriter(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void write_listIsNull_throwsException() {
        //arrange
        AccountTransactionWriter testee = new AccountTransactionWriter(mock(OutputStream.class));
        //act
        testee.write(null);
    }

    @Test
    public void write_standardInput_writesExpectedLines() {
        //arrange
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        List<AccountTransactionDto> transactions = Arrays.asList(
                new AccountTransactionDto(),
                new AccountTransactionDto(),
                new AccountTransactionDto());
        AccountTransactionWriter testee = new AccountTransactionWriter(output);
        //act
        testee.write(transactions);
        //assert
        assertThat(output.toString(), is(TEST_THREE_EMPTY_LINES));
    }

}