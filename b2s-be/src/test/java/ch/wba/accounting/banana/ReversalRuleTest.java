package ch.wba.accounting.banana;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ReversalRuleTest {
    private ReversalRule testee;

    @Before
    public void setUp() {
        testee = new ReversalRule();
    }

    @Test
    public void applicable_notReversal_false() {
        // arrange
        final BananaTransactionDto transaction = new BananaTransactionDto();
        transaction.setVatCode("Foo");
        // act
        final boolean result = testee.applicable(transaction);
        // assert
        assertThat(result, is(false));
    }

    @Test
    public void applicable_reversal_true() {
        // arrange
        final BananaTransactionDto transaction = new BananaTransactionDto();
        transaction.setVatCode("-VSM77");
        // act
        final boolean result = testee.applicable(transaction);
        // assert
        assertThat(result, is(true));
    }

    @Test
    public void applicable_reversal_isReversal() {
        // arrange
        final BananaTransactionDto transaction = new BananaTransactionDto();
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setVatCode("-VSM77");
        // act
        final List<BananaTransactionDto> result = testee.apply(0, Collections.singletonList(transaction));
        // assert
        assertThat(result.get(0).isReversal(), is(true));
        assertThat(result.get(0).getVatCode(), CoreMatchers.is(BananacConstants.VAT_VSM_77_CODE));
        assertThat(result.get(0).getAmount(), CoreMatchers.is(BigDecimal.valueOf(-100)));
    }
}