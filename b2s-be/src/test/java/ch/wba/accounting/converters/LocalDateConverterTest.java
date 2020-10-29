package ch.wba.accounting.converters;

import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LocalDateConverterTest {
    @Test
    public void toString_dayAndMonthLessThen10_dayAndMonthWithoutZiro() {
        // arrange
        final LocalDate date = LocalDate.of(1974, 4, 8);
        //act & assert
        assertThat(LocalDateConverter.toString(date), is("8.4.1974"));
    }

    @Test
    public void toString_dayAndMonthGraterThen10_dayAndMonthAsExpected() {
        // arrange
        final LocalDate date = LocalDate.of(2001, 12, 20);
        //act & assert
        assertThat(LocalDateConverter.toString(date), is("20.12.2001"));
    }
}
