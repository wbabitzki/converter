package ch.wba.account;

import org.apache.commons.lang.Validate;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

public class AccountTransactionWriter {

    private OutputStream output;

    public AccountTransactionWriter(OutputStream output) {
        Validate.notNull(output);
        this.output = output;
    }

    public void write(List<AccountTransactionDto> transactions) {
        Validate.notNull(transactions);

        PrintWriter writer = new PrintWriter(output);
        for (AccountTransactionDto transaction : transactions) {
            writer.println(transaction.toString());
        }
        writer.close();
    }
}
