package ch.wba.accounting.sega;

import ch.wba.accounting.AccountTransactionDto;

import java.util.List;

public interface SegaConverter {

    List<SegaDto> toSegaTransactions(AccountTransactionDto accountTransaction);
}
