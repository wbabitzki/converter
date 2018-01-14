package ch.wba.accounting.sega;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.converters.BigDecimalConverter;
import ch.wba.accounting.converters.LocalDateConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class SegaWithoutTaxConverterTest {

    private SegaConverter testee;

    @Before
    public void setUp() {
        testee = new SegaWithoutTaxConverter();
    }
    
    @Test
    public void toSegaTransactin_bananaTransactionWithoutVat_fieldsAreAssigned() {
        //arrange
        BananaTransactionDto input = new BananaTransactionDto();
        input.setDate(LocalDateConverter.toDate("30.08.2017"));
        input.setDocument("1");
        input.setDescription("Test Description");
        input.setDebitAccount("1171");
        input.setCreditAccount("1020");
        input.setAmount(BigDecimalConverter.toAmount("15000"));
        //act
        List<SegaDto> result = testee.toSegaTransactions(input);
        //assert
        Assert.assertThat(result, hasSize(2));

        assertThat(result.get(0).getDatum(), is(LocalDateConverter.toDate("30.08.2017")));
        assertThat(result.get(0).getBlg(), is("1"));
        assertThat(result.get(0).getTransactionType(), is(SegaDto.SOLL_HABEN.SOLL));
        assertThat(result.get(0).getKto(), is("1171"));
        assertThat(result.get(0).getgKto(), is("1020"));
        assertThat(result.get(0).getsId(), is(""));
        assertThat(result.get(0).getsIdx(), is(0));
        assertThat(result.get(0).getkIndx(), is(0));
        assertThat(result.get(0).getbType(), is(0));
        assertThat(result.get(0).getNetto(), is(new BigDecimal("15000.00")));
        assertThat(result.get(0).getSteuer(), is(new BigDecimal("0")));
        assertThat(result.get(0).getFwBetrag(), is(new BigDecimal("0")));
        assertThat(result.get(0).getTx1(), is("Test Description"));
        assertThat(result.get(0).getTx2(), is(""));

        assertThat(result.get(1).getDatum(), is(LocalDateConverter.toDate("30.08.2017")));
        assertThat(result.get(1).getBlg(), is("1"));
        assertThat(result.get(1).getTransactionType(), is(SegaDto.SOLL_HABEN.HABEN));
        assertThat(result.get(1).getKto(), is("1020"));
        assertThat(result.get(1).getgKto(), is("1171"));
        assertThat(result.get(1).getsId(), is(""));
        assertThat(result.get(1).getsIdx(), is(0));
        assertThat(result.get(1).getkIndx(), is(0));
        assertThat(result.get(1).getbType(), is(0));
        assertThat(result.get(1).getNetto(), is(new BigDecimal("15000.00")));
        assertThat(result.get(1).getSteuer(), is(new BigDecimal("0")));
        assertThat(result.get(1).getFwBetrag(), is(new BigDecimal("0")));
        assertThat(result.get(1).getTx1(), is("Test Description"));
        assertThat(result.get(1).getTx2(), is(""));
    }
}