package ch.wba.accounting.sega;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.opencsv.CSVWriter;

public class SegaWriter {
    private static final String WINDOWS_LINE_END = "\r\n";

    public void write(final Writer writer, final List<SegaDto> list) throws IOException {
        Validate.isTrue(writer != null);
        Validate.isTrue(list != null);
        try (final CSVWriter csvWrite = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, WINDOWS_LINE_END)) {
            csvWrite.writeNext(SegaDto.HEADERS);
            for (final SegaDto sega : list) {
                writer.write(sega.toString() + WINDOWS_LINE_END);
            }
        }
    }
}
