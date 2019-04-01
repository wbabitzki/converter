package ch.wba.accounting.rest;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import ch.wba.accounting.sega.SegaDto;
import ch.wba.accounting.sega.SegaDto.SOLL_HABEN;

public class DefaultObjectMapperTest {
    private DefaultObjectMapper testee;

    @Before
    public void init() {
        testee = new DefaultObjectMapper();
    }

    @Test
    public void serialize_blg_createsBlgProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setBlg("15");
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"Blg\":\"15\""));
    }

    @Test
    public void serialize_datumProvided_createsFilledDatumProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setDatum(LocalDate.of(2019, 4, 8));
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"Datum\":\"8.4.2019\""));
    }

    @Test
    public void serialize_datumNotProvided_createsEmptyDatumProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setDatum(null);
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"Datum\":\"\""));
    }

    @Test
    public void serialize_kto_createsKtoProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setKto("1020");
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"Kto\":\"1020\""));
    }

    @Test
    public void serialize_transactionType_createsSLProperty() throws Exception {
        //arrange
        final SegaDto segaDtoS = new SegaDto();
        final SegaDto segaDtoH = new SegaDto();
        segaDtoS.setTransactionType(SOLL_HABEN.SOLL);
        segaDtoH.setTransactionType(SOLL_HABEN.HABEN);
        //act
        final String resultSoll = testee.writeValueAsString(segaDtoS);
        final String resultHaben = testee.writeValueAsString(segaDtoS);
        //assert
        assertThat(resultSoll, containsString("\"S/H\":\"S\""));
        assertThat(resultHaben, containsString("\"S/H\":\"S\""));
    }

    @Test
    public void serialize_grp_createsGrpProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setGrp(" ");
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"Grp\":\" \""));
    }

    @Test
    public void serialize_gKto_createsGKtoProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setgKto("1020");
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"GKto\":\"1020\""));
    }

    @Test
    public void serialize_sId_createsSIdProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setsId("");
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"SId\":\"\""));
    }

    @Test
    public void serialize_sIdx_createsSIdxProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setsIdx(0);
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"SIdx\":\"0\""));
    }

    @Test
    public void serialize_kIndx_createsKIdxProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setkIndx(0);
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"KIdx\":\"0\""));
    }

    @Test
    public void serialize_bType_createsBTypProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setbType(0);
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"BTyp\":\"0\""));
    }

    @Test
    public void serialize_mType_createsMTypProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setmType(0);
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"MTyp\":\"0\""));
    }

    @Test
    public void serialize_code_createCodeProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setCode("");
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"Code\":\"\\\"\\\"\""));
    }

    @Test
    public void serialize_netto_createNettoProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setNetto(BigDecimal.valueOf(1000));
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"Netto\":\"1000.00\""));
    }

    @Test
    public void serialize_steuer_createSteuerProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setSteuer(BigDecimal.valueOf(0.75));
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"Steuer\":\"0.75\""));
    }

    @Test
    public void serialize_fwBetrag_createFWBetragProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setFwBetrag(BigDecimal.valueOf(0));
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"FW-Betrag\":\"0.00\""));
    }

    @Test
    public void serialize_tx1_createTx1Property() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setTx1("Test1");
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"Tx1\":\"\\\"Test1\\\"\""));
    }

    @Test
    public void serialize_tx2_createTx1Property() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setTx2("Test2");
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"Tx2\":\"\\\"Test2\\\"\""));
    }

    @Test
    public void serialize_pkKey_createPkKeyProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setPkKey(0);
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"PkKey\":\"0\""));
    }

    @Test
    public void serialize_opId_createOpIdProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setOpId("");
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"OpId\":\"\""));
    }

    @Test
    public void serialize_flag_createFlagProperty() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        segaDto.setFlag(0);
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        assertThat(result, containsString("\"Flag\":\"0\""));
    }

    @Test
    public void serialize_emptyDto_containsAllHeaders() throws Exception {
        //arrange
        final SegaDto segaDto = new SegaDto();
        //act
        final String result = testee.writeValueAsString(segaDto);
        //assert
        final String[] headers = { "Blg", "Datum", "Kto", "S/H", "Grp", "GKto", "SId", "SIdx", "KIdx", "BTyp", "MTyp", "Code", //
            "Netto", "Steuer", "FW-Betrag", "Tx1", "Tx2", "PkKey", "OpId", "Flag" };
        for (final String header : headers) {
            assertThat(result, containsString("\"" + header + "\":"));
        }
    }
}
