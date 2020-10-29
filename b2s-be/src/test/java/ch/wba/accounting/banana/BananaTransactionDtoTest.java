package ch.wba.accounting.banana;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BananaTransactionDtoTest {
    private BananaTransactionDto testee;

    private static final File TEST_JSON_COMPOSED_TRANSACTION = new File("src/test/resources/test-composed-transaction.json");

    @Before
    public void setUp() {
        testee = new BananaTransactionDto();
    }

    @Test
    public void isComposedTransaction_noIntegratedTransactions_false() {
        assertFalse(testee.isComposedTransaction());
    }

    @Test
    public void isComposedTransaction_integratedTransactions_true() {
        //arrange
        testee.addIntegratedTransaction(new BananaTransactionDto());
        //act && assert
        assertTrue(testee.isComposedTransaction());
    }

    @Test
    public void isIntegrated_noIntegratedTransactions_false() {
        assertFalse(testee.isIntegrated());
    }

    @Test
    public void isIntegrated_integratedTransactions_true() {
        //arrange
        BananaTransactionDto composed = new BananaTransactionDto();
        composed.addIntegratedTransaction(testee);
        //act && assert
        assertTrue(testee.isIntegrated());
    }

    @Test
    public void getMainTransaction_NotIntegratedTransaction_null() {
        assertNull(testee.getMainTransaction());
    }

    @Test
    public void getMainTransaction_integratedTransactions_returnsMainTransaction() {
        //arrange
        BananaTransactionDto composed = new BananaTransactionDto();
        composed.addIntegratedTransaction(testee);
        //act && assert
        assertThat(testee.getMainTransaction(), is(composed));
    }

    @Test
    public void serialize_composedTransaction_containsBothTransactions() throws Exception {
        //arrange
        ObjectMapper objectMapper = new ObjectMapper();
        final BananaTransactionDto dto = new BananaTransactionDto();
        BananaTransactionDto integrated = new BananaTransactionDto();
        dto.addIntegratedTransaction(integrated);
        //act
        final String result = objectMapper.writeValueAsString(dto);
        //assert
        assertThat(result, allOf(containsString(dto.getUuid().toString()), containsString(integrated.getUuid().toString())));
    }

    @Test
    public void deserialize_composedTransaction_containsBothTransactions() throws Exception {
        //arrange
        ObjectMapper objectMapper = new ObjectMapper();
        //act
        final BananaTransactionDto result = objectMapper.readValue(TEST_JSON_COMPOSED_TRANSACTION, BananaTransactionDto.class);
        //assert
        assertNotNull(result);
        assertThat(result.getIntegratedTransactions().get(0).getMainTransaction(), is(result));
    }
}