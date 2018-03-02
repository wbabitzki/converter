package ch.wba.accounting.sega.converter;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.sega.SegaDto;

public class SegaExpensesWithVatConverter extends AbstractSegaThreeRecordConverter {

    @Override
    protected SegaDto adjustFirstRecord(SegaDto sega, BananaTransactionDto accountTransaction){
        return makeAsVatTransaction(sega, accountTransaction);
    }

    @Override
    protected SegaDto adjustSecondRecord(SegaDto sega, BananaTransactionDto accountTransaction){
        sega.setNetto(accountTransaction.getAmount());
        return sega;
    }

    @Override
    protected SegaDto adjustThirdRecord(SegaDto sega, BananaTransactionDto accountTransaction){
        sega.setTransactionType(SegaDto.SOLL_HABEN.SOLL);
        return sega;
    }
}
