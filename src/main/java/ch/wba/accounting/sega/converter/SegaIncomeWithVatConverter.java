package ch.wba.accounting.sega.converter;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.sega.SegaDto;

public class SegaIncomeWithVatConverter extends AbstractSegaThreeRecordConverter {
    @Override
    protected SegaDto adjustFirstRecord(final SegaDto sega, final BananaTransactionDto accountTransaction) {
        sega.setNetto(accountTransaction.getAmount());
        return sega;
    }

    @Override
    protected SegaDto adjustSecondRecord(final SegaDto sega, final BananaTransactionDto accountTransaction) {
        return makeAsVatTransaction(sega, accountTransaction);
    }

    @Override
    protected SegaDto adjustThirdRecord(final SegaDto sega, final BananaTransactionDto accountTransaction) {
        sega.setTransactionType(SegaDto.SOLL_HABEN.HABEN);
        sega.setgKto(accountTransaction.getDebitAccount());
        return sega;
    }
}
