package ch.wba.account;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AccountTransactionDtoTest {

    @Test
    public void toString_emptyValues_returnsOnlySeparators() {
        //arrange
        AccountTransactionDto testee = new AccountTransactionDto();
        //act
        String result = testee.toString();
        //assert
        assertThat(result, is(";;;;;;;"));
    }

    @Test
    public void toString_allFieldsProvided_returnsCommaSeparatedValues() {
        //arrange
        AccountTransactionDto testee = new AccountTransactionDto();
        testee.setReceipt("001");
        testee.setTransactionDate("08.04.1974");
        testee.setSourceAccount("TEST_SOURCE");
        testee.setTargetAccount("TEST_TARGET");
        testee.setTotalAmount(new BigDecimal("54"));
        testee.setTax(new BigDecimal("4"));
        testee.setAmountBeforeTax(new BigDecimal("50"));
        testee.setDescription("TEST_DESCRIPTION");
        //act
        String result = testee.toString();
        //assert
        assertThat(result, is("001;08.04.1974;TEST_TARGET;TEST_SOURCE;54.00;50.00;4.00;TEST_DESCRIPTION"));
    }
}