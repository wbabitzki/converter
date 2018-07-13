package ch.wba.accounting.converters;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

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
