package ch.wba.accounting.starter;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.banana.BananaTransactionReader;
import ch.wba.accounting.sega.*;
import ch.wba.accounting.sega.converter.SegaComposedConverter;
import ch.wba.accounting.sega.converter.SegaConverter;
import ch.wba.accounting.sega.converter.SegaExpensesWithVatConverter;
import ch.wba.accounting.sega.converter.SegaIncomeWithVatConverter;
import ch.wba.accounting.sega.converter.SegaWithoutTaxConverter;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BananaStarter {
    public static void main(String[] args) throws IOException {
        final InputStream is = BananaStarter.class.getClassLoader().getResourceAsStream("Export Banana - 2017.csv");

        List<BananaTransactionDto> bananaTransaction = new BananaTransactionReader().readTransactions(is);

        ConverterFactory converterFactory = new ConverterFactory();
        converterFactory.register(BananaStarter::isExpensesWithVat, new SegaExpensesWithVatConverter());
        converterFactory.register(BananaStarter::isIncomeWithVat, new SegaIncomeWithVatConverter());
        converterFactory.register(BananaStarter::isWithoutVat, new SegaWithoutTaxConverter());
        converterFactory.register(BananaStarter::isComposed, new SegaComposedConverter());

        List<SegaDto> segaDtos = new ArrayList<>();
        for (BananaTransactionDto transaction : bananaTransaction) {
            SegaConverter converter = converterFactory.create(transaction);
            segaDtos.addAll(converter.toSegaTransactions(transaction));
        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test.csv"), StandardCharsets.UTF_8))) {
            writer.write("Kto,S/H,Grp,GKto,SId,SIdx,KIdx,BTyp,MTyp,Code,Netto,Steuer,FW-Betrag,Tx1,Tx2,PkKey,OpId,Flag\r\n");
            for (SegaDto transaction : segaDtos) {
                writer.write(transaction.toString() + "\r\n");
            }
        }
    }

    private static boolean isExpensesWithVat(BananaTransactionDto transaction) {
        return !isWithoutVat(transaction) && !isComposed(transaction)
                && BigDecimal.ZERO.compareTo(transaction.getAmountVat()) < 0;
    }

    private static boolean isIncomeWithVat(BananaTransactionDto transaction) {
        return !isWithoutVat(transaction) && !isComposed(transaction)
                && BigDecimal.ZERO.compareTo(transaction.getAmountVat()) > 0;
    }

    private static boolean isWithoutVat(BananaTransactionDto transaction) {
        return !isComposed(transaction) && transaction.getAmountVat() == null;
    }

    private static boolean isComposed(BananaTransactionDto transaction) {
        return transaction.isComposedTransaction();
    }
}