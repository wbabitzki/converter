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
        assertThat(result.get(0).toString(), is("11,20.01.2015,1020,H, ,div,,0,0,0,2,\"\",564.20,0.00,0.00,\"UBS, VGA\",\"\",0,,0"));
    }
}