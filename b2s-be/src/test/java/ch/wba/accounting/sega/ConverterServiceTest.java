package ch.wba.accounting.sega;

import ch.wba.accounting.banana.BananaTransactionDto;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConverterServiceTest {

    @Test
    public void isExpensesWithVat_positiveVat_true() {
        //arrange
        BananaTransactionDto transaction = new BananaTransactionDto();
        //act
        transaction.setAmountVat(new BigDecimal(1000));
        //assert
        assertTrue(ConverterService.isExpensesWithVat(transaction));
    }

    @Test
    public void isExpensesWithVat_negativeVat_true() {
        //arrange
        BananaTransactionDto transaction = new BananaTransactionDto();
        //act
        transaction.setAmountVat(new BigDecimal(-1000));
        //assert
        assertFalse(ConverterService.isExpensesWithVat(transaction));
    }

    @Test
    public void isIncomeWithVat_negativeVat_true() {
        //arrange
        BananaTransactionDto transaction = new BananaTransactionDto();
        //act
        transaction.setAmountVat(new BigDecimal(-1000));
        //assert
        assertTrue(ConverterService.isIncomeWithVat(transaction));
    }

    @Test
    public void isIncomeWithVat_positiveVat_true() {
        //arrange
        BananaTransactionDto transaction = new BananaTransactionDto();
        //act
        transaction.setAmountVat(new BigDecimal(1000));
        //assert
        assertFalse(ConverterService.isIncomeWithVat(transaction));
    }

    @Test
    public void isWithoutVat_nullAmountVat_true() {
        //arrange
        BananaTransactionDto transaction = new BananaTransactionDto();
        //act
        transaction.setAmountVat(null);
        //assert
        assertTrue(ConverterService.isWithoutVat(transaction));
    }

    @Test
    public void isWithoutVat_notNullAmountVat_true() {
        //arrange
        BananaTransactionDto transaction = new BananaTransactionDto();
        //act
        transaction.setAmountVat(new BigDecimal(1000));
        //assert
        assertFalse(ConverterService.isWithoutVat(transaction));
    }

    @Test
    public void isComposed_withComposedTransactions_true() {
        //arrange
        BananaTransactionDto transaction = new BananaTransactionDto();
        //act
        transaction.addIntegratedTransaction(new BananaTransactionDto());
        //assert
        assertTrue(ConverterService.isComposed(transaction));
    }

    @Test
    public void isComposed_withoutComposedTransactions_true() {
        //arrange
        BananaTransactionDto transaction = new BananaTransactionDto();
        //act & assert
        assertFalse(ConverterService.isComposed(transaction));
    }
}