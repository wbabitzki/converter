package ch.wba.accounting.banana;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UstRuleTest {
    private UstRule testee = new UstRule();

    @Test
    public void applicable_notUst_false() {
        // arrange
        final BananaTransactionDto transaction = new BananaTransactionDto();
        transaction.setVatCode("Foo");
        // act
        final boolean result = testee.applicable(transaction);
        // assert
        assertThat(result, is(false));
    }

    @Test
    public void applicable_UST77_true() {
        // arrange
        final BananaTransactionDto transaction = new BananaTransactionDto();
        transaction.setVatCode("UST77");
        // act
        final boolean result = testee.applicable(transaction);
        // assert
        assertThat(result, is(true));
    }

    @Test
    public void applicable_UST81_true() {
        // arrange
        final BananaTransactionDto transaction = new BananaTransactionDto();
        transaction.setVatCode("UST81");
        // act
        final boolean result = testee.applicable(transaction);
        // assert
        assertThat(result, is(true));
    }

    @Test
    public void adjust_UST77_changesUstVatCode() {
        //arrange
        final BananaTransactionDto transaction = new BananaTransactionDto();
        transaction.setVatCode("UST77");
        //act
        final List<BananaTransactionDto> result = testee.apply(0, Collections.singletonList(transaction));
        //assert
        assertThat(result.get(0).getVatCode(), CoreMatchers.is(BananacConstants.VAT_UST_77_CODE));
    }

    @Test
    public void adjust_UST81_changesUstVatCode() {
        //arrange
        final BananaTransactionDto transaction = new BananaTransactionDto();
        transaction.setVatCode("UST81");
        //act
        final List<BananaTransactionDto> result = testee.apply(0, Collections.singletonList(transaction));
        //assert
        assertThat(result.get(0).getVatCode(), CoreMatchers.is(BananacConstants.VAT_UST_81_CODE));
    }
}