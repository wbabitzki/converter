package ch.wba.accounting.sega;

import ch.wba.accounting.AccountTransactionDto;
import ch.wba.accounting.banana.BananaTransactionDto;

import java.util.List;

public class SegaIncomeWithVatConverter extends AbstractSegaThreeRecordConverter {
    @Override
    protected SegaDto adjustFirstRecord(SegaDto sega, BananaTransactionDto accountTransaction) {
        sega.setNetto(accountTransaction.getAmount());
        return sega;
    }

    @Override
    protected SegaDto adjustSecondRecord(SegaDto sega, BananaTransactionDto accountTransaction) {
        return makeAsVatTransaction(sega, accountTransaction);
    }

    @Override
    protected SegaDto adjustThirdRecord(SegaDto sega, BananaTransactionDto accountTransaction) {
        sega.setTransactionType(SegaDto.SOLL_HABEN.HABEN);
        return sega;
    }

    @Override
    public List<SegaDto> toSegaTransactions(AccountTransactionDto accountTransaction) {
        throw new UnsupportedOperationException();
    }
}
