package ch.wba.accounting.sega.converter;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.converters.BigDecimalConverter;
import ch.wba.accounting.converters.LocalDateConverter;
import ch.wba.accounting.sega.SegaDto;

public class SegaWithoutVatShortConverterTest {
    private SegaConverter testee;

    @Before
    public void setUp() {
        testee = new SegaWithoutVatShortConverter();
    }

    @Test
    public void toSegaTransactions_standardInput_createsShortRecordWithoutVat() {
        //arrange
        final BananaTransactionDto input = new BananaTransactionDto();
        input.setDate(LocalDateConverter.toDate("20.01.2015"));
        input.setDocument("11");
        input.setDescription("Digital Ocean, div. Mat. USD");
        input.setDebitAccount("4000");
        input.setCreditAccount("1020");
        input.setAmount(BigDecimalConverter.toAmount("15.04"));
        //act
        final List<SegaDto> result = testee.toSegaTransactions(input);
        //assert
        assertThat(result, hasSize(1));
        assertThat(result.get(0).toString(), is("11,20.1.2015,4000,S, ,1020,,0,0,0,2,\"\",15.04,0.00,0.00,\"Digital Ocean, div. Mat. USD\",\"\",0,,0"));
    }
}