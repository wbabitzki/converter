package ch.wba.accounting.sega.converter;

import java.util.Arrays;
import java.util.List;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.sega.SegaDto;

public class SegaWithVatShortConverter extends AbstractSegaConverter {
    @Override
    public List<SegaDto> toSegaTransactions(final BananaTransactionDto transaction) {
        final SegaDto debit = createDefaultSegaDto(transaction);
        debit.setKto(transaction.getDebitAccount());
        debit.setTransactionType(SegaDto.SOLL_HABEN.SOLL);
        debit.setgKto(transaction.getCreditAccount());
        debit.setsId(transaction.getVatCode());
        debit.setNetto(transaction.getAmountWithoutVat());
        debit.setSteuer(transaction.getAmountVat());
        debit.setmType(2);
        debit.setTx2("");
        //
        final SegaDto tax = createDefaultSegaDto(transaction);
        tax.setKto(transaction.getVatAccount());
        tax.setTransactionType(SegaDto.SOLL_HABEN.SOLL);
        tax.setgKto(transaction.getCreditAccount());
        tax.setNetto(transaction.getAmountVat());
        tax.setSteuer(transaction.getAmountWithoutVat());
        tax.setmType(2);
        tax.setbType(2);
        tax.setTx2(transaction.getVatPct().abs() + "%");
        return Arrays.asList(debit, tax);
    }
}
