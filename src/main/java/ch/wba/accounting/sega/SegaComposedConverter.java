package ch.wba.accounting.sega;

import ch.wba.accounting.AccountTransactionDto;
import ch.wba.accounting.banana.BananaTransactionDto;

import java.util.Collections;
import java.util.List;

public class SegaComposedConverter extends AbstractSegaConverter {
    @Override
    public List<SegaDto> toSegaTransactions(AccountTransactionDto accountTransaction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SegaDto> toSegaTransactions(BananaTransactionDto transaction) {
        final SegaDto segaDto = createDefaultSegaDto(transaction);
        segaDto.setKto(transaction.getCreditAccount());
        segaDto.setTransactionType(SegaDto.SOLL_HABEN.HABEN);
        segaDto.setgKto("div");
        segaDto.setNetto(transaction.getAmount());
        segaDto.setmType(2);
        segaDto.setTx2("");
        return Collections.singletonList(segaDto);
    }
}
