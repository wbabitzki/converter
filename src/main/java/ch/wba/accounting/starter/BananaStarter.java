package ch.wba.accounting.starter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.banana.BananaTransactionReader;
import ch.wba.accounting.sega.ConverterService;
import ch.wba.accounting.sega.SegaDto;
import ch.wba.accounting.sega.SegaWriter;

public class BananaStarter {
    public static void main(final String[] args) {
        List<BananaTransactionDto> bananaTransaction = null;
        final String inputEncoding = args.length == 3 ? args[2] : "UTF-8";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(args[0])), inputEncoding))) {
            bananaTransaction = new BananaTransactionReader().readTransactions(reader);
        } catch (final FileNotFoundException e) {
            System.err.println("File cannot be found: " + args[0]);
        } catch (final IOException e) {
            System.err.println("Banana transactions cannot be processed: " + args[0]);
        }
        final List<SegaDto> segaDtos = new ConverterService().convert(bananaTransaction);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8))) {
            new SegaWriter().write(writer, segaDtos);
        } catch (final IOException e) {
            System.err.println("Sega import file cannot be written: " + args[1]);
        }
    }
}