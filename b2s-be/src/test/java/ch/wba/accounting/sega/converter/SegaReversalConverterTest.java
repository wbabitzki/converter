package ch.wba.accounting.sega.converter;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.converters.BigDecimalConverter;
import ch.wba.accounting.converters.LocalDateConverter;
import ch.wba.accounting.sega.SegaDto;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class SegaReversalConverterTest {
    private SegaConverter testee;

    @Before
    public void setUp() {
        testee = new SegaReversalConverter();
    }

    @Test
    public void toSegaTransactions_standardInput_createsReversalRecords() {
        //arrange
        final BananaTransactionDto input = new BananaTransactionDto();
        input.setDate(LocalDateConverter.toDate("20.01.2015"));
        input.setDocument("11");
        input.setDescription("Digital Ocean, div. Mat. USD");
        input.setDebitAccount("6210");
        input.setCreditAccount("1020");
        input.setVatPct(BigDecimalConverter.toPct("-8.0"));
        input.setAmountWithoutVat(BigDecimalConverter.toAmount("-83.35"));
        input.setAmountVat(BigDecimalConverter.toAmount("-6.65"));
        input.setVatCode("VSB80");
        input.setVatAccount("1771");
        //act
        final List<SegaDto> result = testee.toSegaTransactions(input);
        //assert
        assertThat(result, hasSize(2));
        assertThat(result.get(0).toString(), is("11,20.1.2015,1020,S, ,6210,VSB80,0,0,0,2,\"\",83.35,6.65,0.00,\"Digital Ocean, div. Mat. USD\",\"\",0,,0"));
        assertThat(result.get(1).toString(), is("11,20.1.2015,1020,S, ,1771,,0,0,2,2,\"\",6.65,83.35,0.00,\"Digital Ocean, div. Mat. USD\",\"8.0%\",0,,0"));
    }
}