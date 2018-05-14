package ch.wba.accounting.converters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateConverter {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy");

    private LocalDateConverter() {
        // Empty
    }

    public static LocalDate toDate(final String dateAsString) {
        return LocalDate.parse(dateAsString, DATE_FORMATTER);
    }

    public static boolean isDate(final String dateAsString) {
        LocalDate date = null;
        try {
            date = LocalDate.parse(dateAsString, DATE_FORMATTER);
        } catch (final DateTimeParseException e) {
            return false;
        }
        return date != null;
    }

    public static String toString(final LocalDate date) {
        return date.format(DATE_FORMATTER);
    }
}
