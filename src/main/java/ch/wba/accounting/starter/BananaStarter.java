package ch.wba.accounting.starter;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.banana.BananaTransactionReader;
import ch.wba.accounting.sega.*;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BananaStarter {
    public static void main(String[] args) throws IOException {
        final InputStream is = Starter.class.getClassLoader().getResourceAsStream("Export Banana - 2017.csv");

        List<BananaTransactionDto> bananaTransaction = new BananaTransactionReader().readTransactions(is);

        ConverterFactory converterFactory = new ConverterFactory();
        converterFactory.register(BananaStarter::isExpensesWithVat, new SegaExpensesWithVatConverter());
        converterFactory.register(BananaStarter::isIncomeWithVat, new SegaIncomeWithVatConverter());
        converterFactory.register(BananaStarter::isWithoutVat, new SegaWithoutTaxConverter());

        List<SegaDto> segaDtos = new ArrayList<>();
        for (BananaTransactionDto transaction : bananaTransaction) {
            SegaConverter converter = converterFactory.create(transaction);
            segaDtos.addAll(converter.toSegaTransactions(transaction));
        }

        Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("test.csv"), StandardCharsets.UTF_8));
        for (SegaDto transaction : segaDtos) {
            writer.write(transaction.toString() + "");
            writer.write("\n");
        }
        writer.close();
    }

    private static boolean isExpensesWithVat(BananaTransactionDto transaction) {
        return !isWithoutVat(transaction) && BigDecimal.ZERO.compareTo(transaction.getAmountVat()) < 0;
    }

    private static boolean isIncomeWithVat(BananaTransactionDto transaction) {
        return !isWithoutVat(transaction) && BigDecimal.ZERO.compareTo(transaction.getAmountVat()) > 0;
    }

    private static boolean isWithoutVat(BananaTransactionDto transaction) {
        return transaction.getAmountVat() == null;
    }
}