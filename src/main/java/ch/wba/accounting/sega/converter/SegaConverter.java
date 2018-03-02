package ch.wba.accounting.sega.converter;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.sega.SegaDto;

import java.util.List;

public interface SegaConverter {

    List<SegaDto> toSegaTransactions(BananaTransactionDto accountTransaction);
}
