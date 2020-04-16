package ch.wba.accounting.converters;

import org.junit.Test;

import java.math.BigDecimal;

import static ch.wba.accounting.converters.BigDecimalConverter.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BigDecimalConverterTest {
    @Test
    public void toAmount_numberAsString_converts() {
        assertThat(toAmount("200.02"), is(new BigDecimal("200.02")));
        assertThat(toAmount("200"), is(new BigDecimal("200.00")));
    }

    @Test
    public void toAmount_withThousandDelimiters_coverts() {
        assertThat(toAmount("2'000.55"), is(new BigDecimal("2000.55")));
        assertThat(toAmount("2'000"), is(new BigDecimal("2000.00")));
        assertThat(toAmount("5’000.00"), is(new BigDecimal("5000.00")));
        assertThat(toAmount("5’000"), is(new BigDecimal("5000.00")));
    }

    @Test
    public void toPct_numberAsString_converts() {
        assertThat(toPct("200.02"), is(new BigDecimal("200.0")));
        assertThat(toPct("200"), is(new BigDecimal("200.0")));
        assertThat(toPct("2'000.55"), is(new BigDecimal("2000.6")));
        assertThat(toPct("8"), is(new BigDecimal("8.0")));
        assertThat(toPct("8.0"), is(new BigDecimal("8.0")));
        assertThat(toPct("7.7"), is(new BigDecimal("7.7")));
    }

    @Test
    public void toString_numbers_convertsToString() {
        assertThat(asString(new BigDecimal("500.04")), is("500.04"));
        assertThat(asString(new BigDecimal("500.04")), is("500.04"));
        assertThat(asString(new BigDecimal("0.03")), is("0.03"));
        assertThat(asString(new BigDecimal("0.5")), is("0.50"));
        assertThat(asString(new BigDecimal("700.175")), is("700.18"));
    }

    @Test
    public void toString_numbersWithThousands_convertsWithoutDelimiters() {
        assertThat(asString(new BigDecimal("1000")), is("1000.00"));
        assertThat(asString(new BigDecimal("1000.00")), is("1000.00"));
    }
}