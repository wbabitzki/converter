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

public class SegaExpensesWithVatConverterTest {
    private static final String TEST_VAT_ACCOUNT = "1171";
    private static final String TEST_CREDIT_ACCOUNT = "1020";
    private static final String TEST_DEBIT_ACCOUNT = "3000";
    private SegaConverter testee;

    @Before
    public void setUp() {
        testee = new SegaExpensesWithVatConverter();
    }

    @Test
    public void toSegaTransactions_bananaTransactionWithVat_fieldsAreAssigned() {
        //arrange
        final BananaTransactionDto input = new BananaTransactionDto();
        input.setDate(LocalDateConverter.toDate("13.01.2018"));
        input.setDocument("1");
        input.setDescription("Test Description");
        input.setDebitAccount(TEST_DEBIT_ACCOUNT);
        input.setCreditAccount(TEST_CREDIT_ACCOUNT);
        input.setAmount(BigDecimalConverter.toAmount("15000"));
        input.setVatCode("VSB80");
        input.setVatPct(BigDecimalConverter.toPct("8.0"));
        input.setAmountWithoutVat(BigDecimalConverter.toAmount("13888.89"));
        input.setAmountVat(BigDecimalConverter.toAmount("-1111.11"));
        input.setVatAccount(TEST_VAT_ACCOUNT);
        //act
        final List<SegaDto> result = testee.toSegaTransactions(input);
        //assert
        Assert.assertThat(result, hasSize(3));
        assertThat(result.get(0).getBlg(), is("1"));
        assertThat(result.get(0).getTransactionType(), is(SegaDto.SOLL_HABEN.SOLL));
        assertThat(result.get(0).getKto(), is(TEST_DEBIT_ACCOUNT));
        assertThat(result.get(0).getgKto(), is(TEST_CREDIT_ACCOUNT));
        assertThat(result.get(0).getsId(), is("VSB80"));
        assertThat(result.get(0).getsIdx(), is(3));
        assertThat(result.get(0).getkIndx(), is(0));
        assertThat(result.get(0).getbType(), is(0));
        assertThat(result.get(0).getmType(), is(1));
        assertThat(result.get(0).getNetto(), is(new BigDecimal("13888.89")));
        assertThat(result.get(0).getSteuer(), is(new BigDecimal("1111.11")));
        assertThat(result.get(0).getFwBetrag(), is(new BigDecimal("0")));
        assertThat(result.get(0).getTx1(), is("Test Description"));
        assertThat(result.get(0).getTx2(), is(""));
        //
        assertThat(result.get(1).getBlg(), is("1"));
        assertThat(result.get(1).getTransactionType(), is(SegaDto.SOLL_HABEN.HABEN));
        assertThat(result.get(1).getKto(), is(TEST_CREDIT_ACCOUNT));
        assertThat(result.get(1).getgKto(), is(TEST_DEBIT_ACCOUNT));
        assertThat(result.get(1).getsId(), is(""));
        assertThat(result.get(1).getsIdx(), is(0));
        assertThat(result.get(1).getkIndx(), is(0));
        assertThat(result.get(1).getbType(), is(0));
        assertThat(result.get(1).getmType(), is(1));
        assertThat(result.get(1).getNetto(), is(new BigDecimal("15000.00")));
        assertThat(result.get(1).getSteuer(), is(new BigDecimal("0")));
        assertThat(result.get(1).getFwBetrag(), is(new BigDecimal("0")));
        assertThat(result.get(1).getTx1(), is("Test Description"));
        assertThat(result.get(1).getTx2(), is(""));
        //
        assertThat(result.get(2).getBlg(), is("1"));
        assertThat(result.get(2).getTransactionType(), is(SegaDto.SOLL_HABEN.SOLL));
        assertThat(result.get(2).getKto(), is(TEST_VAT_ACCOUNT));
        assertThat(result.get(2).getgKto(), is(TEST_CREDIT_ACCOUNT));
        assertThat(result.get(2).getsId(), is(""));
        assertThat(result.get(2).getsIdx(), is(0));
        assertThat(result.get(2).getkIndx(), is(0));
        assertThat(result.get(2).getbType(), is(2));
        assertThat(result.get(2).getmType(), is(1));
        assertThat(result.get(2).getNetto(), is(new BigDecimal("1111.11")));
        assertThat(result.get(2).getSteuer(), is(new BigDecimal("13888.89")));
        assertThat(result.get(2).getFwBetrag(), is(new BigDecimal("0")));
        assertThat(result.get(2).getTx1(), is("Test Description"));
        assertThat(result.get(2).getTx2(), is("8.0%"));
    }
}