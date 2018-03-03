package ch.wba.accounting.starter;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.banana.BananaTransactionReader;
import ch.wba.accounting.sega.ConverterService;
import ch.wba.accounting.sega.SegaDto;
import ch.wba.accounting.sega.SegaWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class BananaStarter {
    public static void main(String[] args) throws IOException {
        final InputStream is = BananaStarter.class.getClassLoader().getResourceAsStream("Export Banana - 2017.csv");
        final List<BananaTransactionDto> bananaTransaction = new BananaTransactionReader().readTransactions(is);
        final List<SegaDto> segaDtos = new ConverterService().convert(bananaTransaction);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test.csv"), StandardCharsets.UTF_8))) {
            new SegaWriter().write(writer, segaDtos);
        }
    }
}