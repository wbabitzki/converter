package ch.wba.accounting.sega;

import ch.wba.accounting.banana.BananaTransactionDto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

abstract public class AbstractSegaThreeRecordConverter extends AbstractSegaConverter {

    @Override
    public List<SegaDto> toSegaTransactions(BananaTransactionDto accountTransaction) {
        SegaDto debit = createFirstTransaction(accountTransaction);
        SegaDto credit = createSecondTransaction(accountTransaction);
        SegaDto tax = createThirdTransaction(accountTransaction);

        return Arrays.asList(
                adjustFirstRecord(debit, accountTransaction),
                adjustSecondRecord(credit,accountTransaction),
                adjustThirdRecord(tax, accountTransaction));
    }

    private SegaDto createFirstTransaction(BananaTransactionDto accountTransaction) {
        SegaDto sega = createDefaultSegaDto(accountTransaction);
        sega.setKto(accountTransaction.getDebitAccount());
        sega.setTransactionType(SegaDto.SOLL_HABEN.SOLL);
        sega.setgKto(accountTransaction.getCreditAccount());
        return sega;
    }

    private SegaDto createSecondTransaction(BananaTransactionDto accountTransaction) {
        SegaDto sega = createDefaultSegaDto(accountTransaction);
        sega.setKto(accountTransaction.getCreditAccount());
        sega.setTransactionType(SegaDto.SOLL_HABEN.HABEN);
        sega.setgKto(accountTransaction.getDebitAccount());
        sega.setSteuer(BigDecimal.ZERO);
        return sega;
    }

    private SegaDto createThirdTransaction(BananaTransactionDto accountTransaction) {
        SegaDto sega = createDefaultSegaDto(accountTransaction);
        sega.setKto(accountTransaction.getVatAccount());
        sega.setgKto(accountTransaction.getDebitAccount());
        sega.setbType(2);
        sega.setNetto(accountTransaction.getAmountVat().abs());
        sega.setSteuer(accountTransaction.getAmountWithoutVat());
        sega.setTx2(accountTransaction.getVatPct().abs() + "%");
        return sega;
    }

    @Override
    final protected SegaDto createDefaultSegaDto(BananaTransactionDto transaction) {
         final SegaDto  sega = super.createDefaultSegaDto(transaction);
         sega.setmType(1);
         sega.setTx2("");
         return sega;
    }

    final protected SegaDto makeAsVatTransaction(SegaDto sega, BananaTransactionDto accountTransaction) {
        sega.setsId(accountTransaction.getVatCode());
        sega.setsIdx(3);
        sega.setNetto(accountTransaction.getAmountWithoutVat());
        sega.setSteuer(accountTransaction.getAmountVat().abs());
        return sega;
    }

    abstract protected SegaDto adjustFirstRecord(SegaDto sega, BananaTransactionDto accountTransaction);

    abstract protected SegaDto adjustSecondRecord(SegaDto sega, BananaTransactionDto accountTransaction);

    abstract protected SegaDto adjustThirdRecord(SegaDto sega, BananaTransactionDto accountTransaction);
}
