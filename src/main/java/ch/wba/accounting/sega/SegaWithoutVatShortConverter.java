package ch.wba.accounting.sega;

import ch.wba.accounting.AccountTransactionDto;
import ch.wba.accounting.banana.BananaTransactionDto;

import java.util.Collections;
import java.util.List;

public class SegaWithoutVatShortConverter extends AbstractSegaConverter {
    @Override
    public List<SegaDto> toSegaTransactions(final AccountTransactionDto accountTransaction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SegaDto> toSegaTransactions(final BananaTransactionDto transaction) {
        final SegaDto segaDto = createDefaultSegaDto(transaction);
        segaDto.setKto(transaction.getDebitAccount());
        segaDto.setTransactionType(SegaDto.SOLL_HABEN.SOLL);
        segaDto.setgKto(transaction.getCreditAccount());
        segaDto.setNetto(transaction.getAmount());
        segaDto.setmType(2);
        segaDto.setTx2("");
        return Collections.singletonList(segaDto);
    }
}
