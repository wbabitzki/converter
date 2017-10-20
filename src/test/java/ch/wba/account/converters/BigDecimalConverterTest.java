package ch.wba.account.converters;

import org.junit.Test;

import java.math.BigDecimal;

import static ch.wba.account.converters.BigDecimalConverter.toAmount;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BigDecimalConverterTest {

    @Test
    public void toAmount_numberAsString_converts() {
        assertThat(toAmount("200.02"), is(new BigDecimal("200.02")));
        assertThat(toAmount("200"), is(new BigDecimal(200)));
        assertThat(toAmount("2'000.55"), is(new BigDecimal("2000.55")));
    }


}