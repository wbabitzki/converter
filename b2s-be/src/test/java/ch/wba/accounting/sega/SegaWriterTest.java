package ch.wba.accounting.sega;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.endsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class SegaWriterTest {
    private static final String TEST_HEADER = "Blg,Datum,Kto,S/H,Grp,GKto,SId,SIdx,KIdx,BTyp,MTyp,Code,Netto,Steuer,FW-Betrag,Tx1,Tx2,PkKey,OpId,Flag\r\n";
    private static final ArrayList<SegaDto> TEST_EMPTY_LIST = new ArrayList<>();
    private SegaWriter testee;

    @Before
    public void setUp() {
        testee = new SegaWriter();
    }

    @Test(expected = IllegalArgumentException.class)
    public void write_writerIsNull_throwsException() throws IOException {
        //arrange
        testee.write(null, TEST_EMPTY_LIST);
    }

    @Test(expected = IllegalArgumentException.class)
    public void write_listIsNull_throwsException() throws IOException {
        //arrange
        testee.write(mock(Writer.class), null);
    }

    @Test
    public void write_standardInput_writesHeader() throws IOException {
        //arrange
        final Writer writer = mock(Writer.class);
        //act
        testee.write(writer, TEST_EMPTY_LIST);
        //assert
        verify(writer).write(TEST_HEADER);
    }

    @Test
    public void write_threeSegaDto_writeThreeLines() throws IOException {
        //arrange
        final Writer writer = mock(Writer.class);
        final List<SegaDto> list = Arrays.asList(createSegaDto(), createSegaDto(), createSegaDto());
        //act
        testee.write(writer, list);
        //assert
        verify(writer).write(TEST_HEADER);
        verify(writer, times(4)).write(endsWith("\r\n"));
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
