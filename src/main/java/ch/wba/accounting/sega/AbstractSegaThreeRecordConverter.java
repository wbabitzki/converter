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
        sega.setNetto(accountTransaction.getAmountWithoutVat());
        return sega;
    }

    private SegaDto createSecondTransaction(BananaTransactionDto accountTransaction) {
        SegaDto sega = createDefaultSegaDto(accountTransaction);
        sega.setKto(accountTransaction.getCreditAccount());
        sega.setTransactionType(SegaDto.SOLL_HABEN.HABEN);
        sega.setgKto(accountTransaction.getDebitAccount());
        sega.setNetto(accountTransaction.getAmount());
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
    protected SegaDto createDefaultSegaDto(BananaTransactionDto transaction) {
         final SegaDto  sega = super.createDefaultSegaDto(transaction);
         sega.setmType(1);
         sega.setTx2("");
         return sega;
    }

    abstract protected SegaDto adjustFirstRecord(SegaDto sega, BananaTransactionDto accountTransaction);

    abstract protected SegaDto adjustSecondRecord(SegaDto sega, BananaTransactionDto accountTransaction);

    abstract protected SegaDto adjustThirdRecord(SegaDto sega, BananaTransactionDto accountTransaction);
}
