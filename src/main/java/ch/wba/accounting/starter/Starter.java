package ch.wba.accounting.starter;

import ch.wba.accounting.AccountTransactionDto;
import ch.wba.accounting.AccountTransactionWriter;
import ch.wba.accounting.converters.TransactionConverter;
import ch.wba.accounting.ubs.UbsTransactionDto;
import ch.wba.accounting.ubs.UbsTransactionReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Starter {
    public static void main(String[] args) throws IOException {
        final InputStream is = Starter.class.getClassLoader().getResourceAsStream("transactions.csv");

        List<UbsTransactionDto> ubsTransaction = new UbsTransactionReader().readTransactions(is);
        List<AccountTransactionDto> accountTransactions = new TransactionConverter().convert(ubsTransaction);
        for (AccountTransactionDto accountTransaction : accountTransactions) {
            System.out.println(accountTransaction.toString());
        }

        File file = new File("test.csv");
        try (FileOutputStream output = new FileOutputStream(file)) {
            new AccountTransactionWriter(output).write(accountTransactions);
        }

    }
}