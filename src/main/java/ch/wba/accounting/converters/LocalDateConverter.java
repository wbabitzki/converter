package ch.wba.accounting.converters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateConverter {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static LocalDate toDate(String dateAsString) {
        return  LocalDate.parse(dateAsString, DATE_FORMATTER);
    }

    public static boolean isDate(String dateAsString) {
        LocalDate date = null;
        try {
            date = LocalDate.parse(dateAsString, DATE_FORMATTER);
        }  catch (DateTimeParseException e) {
           return false;
        }
        return date != null;
    }

    public static String toString(LocalDate date) {
        return  date.format(DATE_FORMATTER);
    }
}
