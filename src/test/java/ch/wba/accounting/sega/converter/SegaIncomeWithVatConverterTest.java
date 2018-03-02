package ch.wba.accounting.sega.converter;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.converters.BigDecimalConverter;
import ch.wba.accounting.converters.LocalDateConverter;
import ch.wba.accounting.sega.SegaDto;

public class SegaIncomeWithVatConverterTest {
    private SegaConverter testee;

    @Before
    public void setUp() {
        testee = new SegaIncomeWithVatConverter();
    }

    @Test
    public void toSegaTransactions_bananaTransactionWithVat_fieldsAreAssigned() {
        //arrange
        final BananaTransactionDto input = new BananaTransactionDto();
        input.setDate(LocalDateConverter.toDate("13.01.2018"));
        input.setDocument("1");
        input.setDescription("Test Description");
        input.setDebitAccount("1020");
        input.setCreditAccount("3400");
        input.setAmount(BigDecimalConverter.toAmount("15000"));
        input.setVatCode("USt80");
        input.setVatPct(BigDecimalConverter.toAmount("0.08"));
        input.setAmountWithoutVat(BigDecimalConverter.toAmount("13888.89"));
        input.setAmountVat(BigDecimalConverter.toAmount("-1111.11"));
        input.setVatAccount("2200");
        //act
        final List<SegaDto> result = testee.toSegaTransactions(input);
        //assert
        Assert.assertThat(result, hasSize(3));
        assertThat(result.get(0).getBlg(), is("1"));
        assertThat(result.get(0).getTransactionType(), is(SegaDto.SOLL_HABEN.SOLL));
        assertThat(result.get(0).getKto(), is("1020"));
        assertThat(result.get(0).getgKto(), is("3400"));
        assertThat(result.get(0).getsId(), is(""));
        assertThat(result.get(0).getsIdx(), is(0));
        assertThat(result.get(0).getkIndx(), is(0));
        assertThat(result.get(0).getbType(), is(0));
        assertThat(result.get(0).getmType(), is(1));
        assertThat(result.get(0).getNetto(), is(new BigDecimal("15000.00")));
        assertThat(result.get(0).getSteuer(), is(BigDecimal.ZERO));
        assertThat(result.get(0).getFwBetrag(), is(BigDecimal.ZERO));
        assertThat(result.get(0).getTx1(), is("Test Description"));
        assertThat(result.get(0).getTx2(), is(""));
        assertThat(result.get(1).getBlg(), is("1"));
        assertThat(result.get(1).getTransactionType(), is(SegaDto.SOLL_HABEN.HABEN));
        assertThat(result.get(1).getKto(), is("3400"));
        assertThat(result.get(1).getgKto(), is("1020"));
        assertThat(result.get(1).getsId(), is("USt80"));
        assertThat(result.get(1).getsIdx(), is(3));
        assertThat(result.get(1).getkIndx(), is(0));
        assertThat(result.get(1).getbType(), is(0));
        assertThat(result.get(1).getmType(), is(1));
        assertThat(result.get(1).getNetto(), is(new BigDecimal("13888.89")));
        assertThat(result.get(1).getSteuer(), is(new BigDecimal("1111.11")));
        assertThat(result.get(1).getFwBetrag(), is(BigDecimal.ZERO));
        assertThat(result.get(1).getTx1(), is("Test Description"));
        assertThat(result.get(1).getTx2(), is(""));
        assertThat(result.get(2).getBlg(), is("1"));
        assertThat(result.get(2).getTransactionType(), is(SegaDto.SOLL_HABEN.HABEN));
        assertThat(result.get(2).getKto(), is("2200"));
        assertThat(result.get(2).getgKto(), is("1020"));
        assertThat(result.get(2).getsId(), is(""));
        assertThat(result.get(2).getsIdx(), is(0));
        assertThat(result.get(2).getkIndx(), is(0));
        assertThat(result.get(2).getbType(), is(2));
        assertThat(result.get(2).getmType(), is(1));
        assertThat(result.get(2).getNetto(), is(new BigDecimal("1111.11")));
        assertThat(result.get(2).getSteuer(), is(new BigDecimal("13888.89")));
        assertThat(result.get(2).getFwBetrag(), is(BigDecimal.ZERO));
        assertThat(result.get(2).getTx1(), is("Test Description"));
        assertThat(result.get(2).getTx2(), is("0.08%"));
    }
}