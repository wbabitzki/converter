package converters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static LocalDate toDate(String dateAsString) {
        return  LocalDate.parse(dateAsString, DATE_FORMATTER);
    }
}
