package ch.wba.accounting.sega;

import ch.wba.accounting.banana.BananaTransactionDto;

import java.util.List;

public interface SegaConverter {

    List<SegaDto> toSegaTransactions(BananaTransactionDto accountTransaction);
}
