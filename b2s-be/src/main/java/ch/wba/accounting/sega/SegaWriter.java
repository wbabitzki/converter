package ch.wba.accounting.sega;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;

import com.opencsv.CSVWriter;

public class SegaWriter {
    private static final String WINDOWS_LINE_END = "\r\n";

    public final void write(final Writer writer, final List<SegaDto> list) throws IOException {
        Validate.isTrue(writer != null);
        Validate.isTrue(list != null);
        try (final CSVWriter csvWrite = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, WINDOWS_LINE_END)) {
            for (final String line : toStringList(list)) {
                writer.write(line);
            }
        }
    }

    public List<String> toStringList(final List<SegaDto> list) {
        final List<String> result = new ArrayList<>(Arrays.asList(String.join(SegaDto.DELIMITER, SegaDto.HEADERS) + WINDOWS_LINE_END));
        result.addAll( //
            list.stream().map(sega -> sega.toString() + WINDOWS_LINE_END) //
                .collect(Collectors.toList()));
        return result;
    }
}
