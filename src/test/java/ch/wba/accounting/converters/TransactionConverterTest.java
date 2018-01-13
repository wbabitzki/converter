package ch.wba.accounting.converters;

import ch.wba.accounting.AccountTransactionDto;
import ch.wba.accounting.ubs.UbsTransactionDto;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TransactionConverterTest {

    @Test(expected = IllegalArgumentException.class)
    public void convert_null_throwsException() {
        //arrange
        TransactionConverter testee = new TransactionConverter();
        //act
        testee.convert(null);
        //assert
    }

    @Test
    public void convert_ubsTransactionsWithoutBalance_transactionsAreIgnored() {
        //arrange
        List<UbsTransactionDto> ubsTransactions = Arrays.asList(
                createUbsTransactionDto(null, "300", null),
                createUbsTransactionDto("300.00", null, null));
        TransactionConverter testee = new TransactionConverter();
        //act
        List<AccountTransactionDto> convertedTransactions = testee.convert(ubsTransactions);
        //assert
        assertThat(convertedTransactions.size(), is(0));
    }

    @Test
    public void convert_oneValidUbsTransaction_fieldsAreAssigned() {
        //arrange
        UbsTransactionDto ubsTransaction = createUbsTransactionDto("54", null, "300");
        ubsTransaction.setTradeDate("08.04.1974");
        ubsTransaction.setDescription2("Test Description");
        TransactionConverter testee = new TransactionConverter();
        //act
        List<AccountTransactionDto> convertedTransactions = testee.convert(Collections.singletonList(ubsTransaction));
        //assert
        assertThat(convertedTransactions.size(), is(1));

        AccountTransactionDto transaction = convertedTransactions.get(0);

        assertThat(transaction.getReceipt(), is("1"));
        assertThat(transaction.getTransactionDate(), is(LocalDateConverter.toDate("08.04.1974")));
        assertThat(transaction.getDescription(), is("Test Description"));
        assertThat(transaction.getTotalAmount(), is(new BigDecimal("54.00")));
        assertThat(transaction.getTax(), is(new BigDecimal("4.00")));
        assertThat(transaction.getAmountBeforeTax(), is(new BigDecimal("50.00")));
    }

    private UbsTransactionDto createUbsTransactionDto(String debit, String credit, String balance) {
        UbsTransactionDto dto = new UbsTransactionDto();
        if (debit !=null) {
            dto.setDebit(debit);
        }

        if (credit != null) {
            dto.setCredit(credit);
        }

        if (balance != null) {
            dto.setBalance(balance);
        }
        
        return dto;
    }
}