package ch.wba.accounting.rest;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.banana.BananaTransactionReader;
import ch.wba.accounting.sega.ConverterService;
import ch.wba.accounting.sega.SegaDto;
import ch.wba.accounting.sega.SegaWriter;

@RunWith(MockitoJUnitRunner.class)
public class BananaResourceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @InjectMocks
    private BananaResource testee;
    @Mock
    BananaTransactionReader bananaReader;
    @Mock
    ConverterService converterService;
    @Mock
    SegaWriter segaWriter;

    @Test
    @SuppressWarnings("unchecked")
    public void readFile_ioException_throwsRuntimeException() {
        //arrange && assert
        final InputStream input = mock(InputStream.class);
        when(bananaReader.readTransactions(any(BufferedReader.class))).thenThrow(IOException.class);
        thrown.expect(RuntimeException.class);
        //act
        testee.readFile(input);
    }

    @Test
    public void readFile_validParams_callsBananaReader() {
        //arrange
        final InputStream input = mock(InputStream.class);
        //act
        testee.readFile(input);
        //assert
        verify(bananaReader).readTransactions(any(BufferedReader.class));
    }

    @Test
    public void convert_fromBanana_createsSegaTransaction() {
        //arrange
        final List<BananaTransactionDto> bananaTransactions = Arrays.asList(new BananaTransactionDto(), new BananaTransactionDto());
        //act
        testee.convert(bananaTransactions);
        //assert
        verify(converterService).convert(bananaTransactions);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void toStringOutput_listSegaDto_createsListString() {
        //arrange
        final List<SegaDto> list = Arrays.asList(createSegaDto(), createSegaDto(), createSegaDto());
        //act
        testee.toStringOutput(list);
        //assert
        verify(segaWriter).toStringList(anyList());
    }

    private SegaDto createSegaDto() {
        final SegaDto segaDto = new SegaDto();
        segaDto.setDatum(LocalDate.now());
        segaDto.setTransactionType(SegaDto.SOLL_HABEN.HABEN);
        segaDto.setNetto(BigDecimal.ZERO);
        segaDto.setSteuer(BigDecimal.ZERO);
        segaDto.setFwBetrag(BigDecimal.ZERO);
        return segaDto;
    }
}
