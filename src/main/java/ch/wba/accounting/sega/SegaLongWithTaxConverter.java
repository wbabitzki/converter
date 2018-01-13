package ch.wba.accounting.sega;

import ch.wba.accounting.AccountTransactionDto;
import ch.wba.accounting.banana.BananaTransactionDto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class SegaLongWithTaxConverter implements SegaConverter {
    @Override
    public List<SegaDto> toSegaTransactions(BananaTransactionDto accountTransaction) {
        SegaDto debit = createDefaultSegaDto(accountTransaction);
        SegaDto credit = createDefaultSegaDto(accountTransaction);
        SegaDto tax = createDefaultSegaDto(accountTransaction);

        debit.setKto(accountTransaction.getDebitAccount());
        debit.setTransactionType(SegaDto.SOLL_HABEN.SOLL);
        debit.setgKto(accountTransaction.getCreditAccount());
        debit.setsId(accountTransaction.getVatCode());
        debit.setsIdx(3);
        debit.setbType(0);
        debit.setNetto(accountTransaction.getAmountWithoutVat());
        debit.setSteuer(accountTransaction.getAmountVat().abs());
        debit.setmType(1);
        debit.setTx2("");

        credit.setKto(accountTransaction.getCreditAccount());
        credit.setTransactionType(SegaDto.SOLL_HABEN.HABEN);
        credit.setgKto(accountTransaction.getDebitAccount());
        credit.setsId("");
        credit.setNetto(accountTransaction.getAmount());
        credit.setSteuer(BigDecimal.ZERO);
        credit.setTx2("");

        tax.setTransactionType(SegaDto.SOLL_HABEN.SOLL);
        tax.setKto(accountTransaction.getVatAccount());
        tax.setgKto(accountTransaction.getDebitAccount());
        tax.setsId("");
        tax.setbType(2);
        tax.setNetto(accountTransaction.getAmountVat().abs());
        tax.setSteuer(accountTransaction.getAmountWithoutVat());
        tax.setTx2(accountTransaction.getVatPct().multiply(new BigDecimal(100)).setScale(0) + "%");
        return Arrays.asList(debit, credit, tax);
    }

    @Override
    public List<SegaDto> toSegaTransactions(AccountTransactionDto accountTransaction) {
        SegaDto debit = createDefaultSegaDto(accountTransaction);
        SegaDto credit = createDefaultSegaDto(accountTransaction);;
        SegaDto tax = createDefaultSegaDto(accountTransaction);;

        debit.setKto(accountTransaction.getTargetAccount());
        debit.setTransactionType(SegaDto.SOLL_HABEN.SOLL);
        debit.setgKto(accountTransaction.getSourceAccount());
        debit.setsId("VSB80");
        debit.setsIdx(3);
        debit.setbType(0);
        debit.setNetto(accountTransaction.getAmountBeforeTax());
        debit.setSteuer(accountTransaction.getTax());
        debit.setmType(1);
        debit.setNetto(accountTransaction.getAmountBeforeTax());
        debit.setSteuer(accountTransaction.getTax());
        debit.setTx2("");

        credit.setKto(accountTransaction.getSourceAccount());
        credit.setTransactionType(SegaDto.SOLL_HABEN.HABEN);
        credit.setgKto(accountTransaction.getTargetAccount());
        credit.setsId("");
        credit.setNetto(accountTransaction.getTotalAmount());
        credit.setSteuer(BigDecimal.ZERO);
        credit.setTx2("");

        tax.setTransactionType(SegaDto.SOLL_HABEN.SOLL);
        tax.setKto("1171");
        tax.setgKto(accountTransaction.getSourceAccount());
        tax.setsId("");
        tax.setbType(2);
        tax.setNetto(accountTransaction.getTax());
        tax.setSteuer(accountTransaction.getAmountBeforeTax());
        tax.setTx2("8%");

        return Arrays.asList(debit, credit, tax);
    }

    private SegaDto createDefaultSegaDto(AccountTransactionDto accountTransaction) {
        SegaDto defaultDto = new SegaDto();
        
        defaultDto.setBlg(accountTransaction.getReceipt());
        defaultDto.setDatum(accountTransaction.getTransactionDate());
        defaultDto.setsId(accountTransaction.getsId());
        defaultDto.setmType(0);
        defaultDto.setFwBetrag(new BigDecimal("0"));
        defaultDto.setTx1(accountTransaction.getDescription());

        return defaultDto;
    }

    private SegaDto createDefaultSegaDto(BananaTransactionDto transaction) {
        SegaDto defaultDto = new SegaDto();

        defaultDto.setBlg(transaction.getDocument());
        defaultDto.setDatum(transaction.getDate());
        defaultDto.setsId(transaction.getVatCode());
        defaultDto.setFwBetrag(new BigDecimal("0"));
        defaultDto.setTx1(transaction.getDescription());

        return defaultDto;
    }
}
