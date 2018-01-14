package ch.wba.accounting.sega;

import ch.wba.accounting.AccountTransactionDto;
import ch.wba.accounting.banana.BananaTransactionDto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class SegaWithoutTaxConverter extends AbstractSegaConverter {
    @Override
    public List<SegaDto> toSegaTransactions(AccountTransactionDto accountTransaction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SegaDto> toSegaTransactions(BananaTransactionDto transaction) {
        SegaDto debit = createDefaultSegaDto(transaction);
        SegaDto credit = createDefaultSegaDto(transaction);

        debit.setKto(transaction.getDebitAccount());
        debit.setTransactionType(SegaDto.SOLL_HABEN.SOLL);
        debit.setgKto(transaction.getCreditAccount());
        debit.setsId("");
        debit.setsIdx(0);
        debit.setbType(0);
        debit.setNetto(transaction.getAmount());
        debit.setSteuer(BigDecimal.ZERO);
        debit.setmType(1);
        debit.setTx2("");

        credit.setKto(transaction.getCreditAccount());
        credit.setTransactionType(SegaDto.SOLL_HABEN.HABEN);
        credit.setgKto(transaction.getDebitAccount());
        credit.setsId("");
        credit.setsIdx(0);
        credit.setbType(0);
        credit.setNetto(transaction.getAmount());
        credit.setSteuer(BigDecimal.ZERO);
        credit.setmType(1);
        credit.setTx2("");

        return Arrays.asList(debit, credit);
    }
}
