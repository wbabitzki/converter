package ch.wba.accounting.sega;

import ch.wba.accounting.AccountTransactionDto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class SegaLongWithTaxConverter implements SegaConverter {
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
}
