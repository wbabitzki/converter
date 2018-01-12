package ch.wba.accounting.converters;

import org.junit.Test;

import java.math.BigDecimal;

import static ch.wba.accounting.converters.BigDecimalConverter.asString;
import static ch.wba.accounting.converters.BigDecimalConverter.toAmount;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BigDecimalConverterTest {

    @Test
    public void toAmount_numberAsString_converts() {
        assertThat(toAmount("200.02"), is(new BigDecimal("200.02")));
        assertThat(toAmount("200"), is(new BigDecimal("200")));
        assertThat(toAmount("2'000.55"), is(new BigDecimal("2000.55")));
    }

    @Test
    public void toString_numbers_convertsToString() {
        assertThat(asString(new BigDecimal("500.04")), is("500.04"));
        assertThat(asString(new BigDecimal("500.04")), is("500.04"));
        assertThat(asString(new BigDecimal("0.03")), is("0.03"));
        assertThat(asString(new BigDecimal("0.5")), is("0.50"));
        assertThat(asString(new BigDecimal("700.175")), is("700.18"));
    }

}