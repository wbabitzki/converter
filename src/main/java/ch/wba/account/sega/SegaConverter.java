package ch.wba.account.sega;

import ch.wba.account.AccountTransactionDto;

import java.util.List;

public interface SegaConverter {

    List<SegaDto> toSegaTransactions(AccountTransactionDto accountTransaction);
}
