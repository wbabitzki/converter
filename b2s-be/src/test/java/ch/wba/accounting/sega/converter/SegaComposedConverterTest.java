package ch.wba.accounting.sega.converter;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.converters.BigDecimalConverter;
import ch.wba.accounting.converters.LocalDateConverter;
import ch.wba.accounting.sega.SegaDto;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class SegaComposedConverterTest {
    private SegaConverter testee;

    @Before
    public void setUp() {
        testee = new SegaComposedConverter();
    }

    @Test
    public void toSegaTransactions_standardInput_createsComposedRecord() {
        //arrange
        final BananaTransactionDto input = new BananaTransactionDto();
        input.setDate(LocalDateConverter.toDate("20.01.2015"));
        input.setDocument("11");
        input.setDescription("UBS, VGA");
        input.setCreditAccount("1020");
        input.setDebitAccount("");
        input.setAmount(BigDecimalConverter.toAmount("564.2"));
        //act
        final List<SegaDto> result = testee.toSegaTransactions(input);
        //assert
        assertThat(result, hasSize(1));
        assertThat(result.get(0).toString(), is("11,20.1.2015,1020,H, ,div,,0,0,0,2,\"\",564.20,0.00,0.00,\"UBS, VGA\",\"\",0,,0"));
    }

    @Test
    public void toSegaTransactions_integratedWithVat_createsComposedRecord() {
        //arrange
        final BananaTransactionDto input = new BananaTransactionDto();
        input.setDate(LocalDateConverter.toDate("20.01.2015"));
        input.setAmount(BigDecimal.valueOf(93.00));

        final BananaTransactionDto integrated = new BananaTransactionDto();
        input.addIntegratedTransaction(integrated);

        integrated.setDate(LocalDateConverter.toDate("20.01.2015"));
        integrated.setDocument("11");
        integrated.setDescription("Tankstelle Podesser, Benzin");
        integrated.setDebitAccount("6210");
        integrated.setAmount(BigDecimal.valueOf(93.00));
        integrated.setAmountWithoutVat(BigDecimal.valueOf(86.35));
        integrated.setVatCode("VSB77");
        integrated.setAmountVat(BigDecimal.valueOf(6.65));
        integrated.setVatPct(BigDecimal.valueOf(7.7));
        //act
        final List<SegaDto> result = testee.toSegaTransactions(input);
        //assert
        assertThat(result, hasSize(3));
        assertThat(result.get(1).toString(), is("11,20.1.2015,6210,S, ,,VSB77,2,0,0,2,\"\",86.35,6.65,0.00,\"Tankstelle Podesser, Benzin\",\"\",0,,0"));
        assertThat(result.get(2).toString(), is("11,20.1.2015,,S, ,,,0,0,2,2,\"\",6.65,86.35,0.00,\"Tankstelle Podesser, Benzin\",\"7.7%\",0,,0"));
    }

    @Test
    public void toSegaTransactions_integratedWithoutVat_createsComposedRecord() {
        //arrange
        final BananaTransactionDto input = new BananaTransactionDto();
        input.setDate(LocalDateConverter.toDate("20.01.2015"));
        input.setAmount(BigDecimal.valueOf(93.00));

        final BananaTransactionDto integrated = new BananaTransactionDto();
        input.addIntegratedTransaction(integrated);

        integrated.setDate(LocalDateConverter.toDate("20.01.2015"));
        integrated.setDocument("11");
        integrated.setDescription("Digital Ocean");
        integrated.setDebitAccount("4000");
        integrated.setAmount(BigDecimal.valueOf(9.95));
        //act
        final List<SegaDto> result = testee.toSegaTransactions(input);
        //assert
        assertThat(result, hasSize(2));
        assertThat(result.get(1).toString(), is("11,20.1.2015,4000,S, ,,,0,0,0,2,\"\",9.95,0.00,0.00,\"Digital Ocean\",\"\",0,,0"));
    }

    @Test
    public void toSegaTransactions_reversalWithVat_createsComposedRecord() {
        //arrange
        final BananaTransactionDto input = new BananaTransactionDto();
        input.setDate(LocalDateConverter.toDate("20.01.2015"));
        input.setAmount(BigDecimal.valueOf(93.00));

        final BananaTransactionDto integrated = new BananaTransactionDto();
        input.addIntegratedTransaction(integrated);

        integrated.setDate(LocalDateConverter.toDate("20.01.2015"));
        integrated.setDocument("11");
        integrated.setDescription("Tankstelle Podesser, Benzin");
        integrated.setDebitAccount("6210");
        integrated.setAmount(BigDecimal.valueOf(-93.00));
        integrated.setAmountWithoutVat(BigDecimal.valueOf(-86.35));
        integrated.setVatCode("VSM77");
        integrated.setAmountVat(BigDecimal.valueOf(-6.65));
        integrated.setVatPct(BigDecimal.valueOf(7.7));
        integrated.setReversal(true);
        //act
        final List<SegaDto> result = testee.toSegaTransactions(input);
        //assert
        assertThat(result, hasSize(3));
        assertThat(result.get(1).toString(), is("11,20.1.2015,,S, ,6210,VSM77,2,0,0,2,\"\",86.35,6.65,0.00,\"Tankstelle Podesser, Benzin\",\"\",0,,0"));
        assertThat(result.get(2).toString(), is("11,20.1.2015,,S, ,,,0,0,2,2,\"\",6.65,86.35,0.00,\"Tankstelle Podesser, Benzin\",\"7.7%\",0,,0"));
    }
}