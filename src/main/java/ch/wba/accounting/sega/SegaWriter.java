package ch.wba.accounting.sega;

import com.opencsv.CSVWriter;
import org.apache.commons.lang.Validate;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class SegaWriter {

    private static final String WINDOWS_LINE_END = "\r\n";

    public void write(final Writer writer, final List<SegaDto> list) throws IOException {
        Validate.notNull(writer);
        Validate.notNull(list);
        try (final CSVWriter csvWrite = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, WINDOWS_LINE_END)) {
            csvWrite.writeNext(SegaDto.HEADERS);
            for (final SegaDto sega : list) {
                writer.write(sega.toString() + WINDOWS_LINE_END);
            }
        }
    }
}
