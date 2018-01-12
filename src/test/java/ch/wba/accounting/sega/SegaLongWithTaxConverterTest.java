package ch.wba.accounting.sega;

import ch.wba.accounting.AccountTransactionDto;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class SegaLongWithTaxConverterTest {

    @Test
    public void toSegaTransactions_standardInput_createsThreeRecords() {
        //arrange
        AccountTransactionDto input = new AccountTransactionDto();
        input.setReceipt("15");
        input.setTargetAccount("6510");
        input.setSourceAccount("1020");
        input.setTotalAmount(new BigDecimal("54.00"));
        input.setAmountBeforeTax(new BigDecimal("50.00"));
        input.setTax(new BigDecimal("4.00"));
        input.setDescription("launchswiss, Festnetz 2/17");

        SegaConverter testee = new SegaLongWithTaxConverter();
        //act
        List<SegaDto> result = testee.toSegaTransactions(input);
        //assert
        assertThat(result, hasSize(3));

        assertThat(result.get(0).getBlg(), is("15"));
        assertThat(result.get(0).getTransactionType(), is(SegaDto.SOLL_HABEN.SOLL));
        assertThat(result.get(0).getKto(), is("6510"));
        assertThat(result.get(0).getgKto(), is("1020"));
        assertThat(result.get(0).getsId(), is("VSB80"));
        assertThat(result.get(0).getsIdx(), is(3));
        assertThat(result.get(0).getkIndx(), is(0));
        assertThat(result.get(0).getbType(), is(0));
        assertThat(result.get(0).getNetto(), is(new BigDecimal("50.00")));
        assertThat(result.get(0).getSteuer(), is(new BigDecimal("4.00")));
        assertThat(result.get(0).getFwBetrag(), is(new BigDecimal("0")));
        assertThat(result.get(0).getTx1(), is("launchswiss, Festnetz 2/17"));
        assertThat(result.get(0).getTx2(), is(""));

        assertThat(result.get(1).getBlg(), is("15"));
        assertThat(result.get(1).getTransactionType(), is(SegaDto.SOLL_HABEN.HABEN));
        assertThat(result.get(1).getKto(), is("1020"));
        assertThat(result.get(1).getgKto(), is("6510"));
        assertThat(result.get(1).getsId(), is(""));
        assertThat(result.get(1).getsIdx(), is(0));
        assertThat(result.get(1).getkIndx(), is(0));
        assertThat(result.get(1).getbType(), is(0));
        assertThat(result.get(1).getNetto(), is(new BigDecimal("54.00")));
        assertThat(result.get(1).getSteuer(), is(new BigDecimal("0")));
        assertThat(result.get(1).getFwBetrag(), is(new BigDecimal("0")));
        assertThat(result.get(1).getTx1(), is("launchswiss, Festnetz 2/17"));
        assertThat(result.get(1).getTx2(), is(""));

        assertThat(result.get(2).getBlg(), is("15"));
        assertThat(result.get(2).getTransactionType(), is(SegaDto.SOLL_HABEN.SOLL));
        assertThat(result.get(2).getKto(), is("1171"));
        assertThat(result.get(2).getgKto(), is("1020"));
        assertThat(result.get(2).getsId(), is(""));
        assertThat(result.get(2).getsIdx(), is(0));
        assertThat(result.get(2).getkIndx(), is(0));
        assertThat(result.get(2).getbType(), is(2));
        assertThat(result.get(2).getNetto(), is(new BigDecimal("4.00")));
        assertThat(result.get(2).getSteuer(), is(new BigDecimal("50.00")));
        assertThat(result.get(2).getFwBetrag(), is(new BigDecimal("0")));
        assertThat(result.get(2).getTx1(), is("launchswiss, Festnetz 2/17"));
        assertThat(result.get(2).getTx2(), is("8%"));
    }
}