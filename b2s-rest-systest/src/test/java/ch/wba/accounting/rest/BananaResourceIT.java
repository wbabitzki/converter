package ch.wba.accounting.rest;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.*;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.Before;
import org.junit.Test;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.banana.BananaTransactionReader;
import ch.wba.accounting.sega.SegaDto;

public class BananaResourceIT {
    static final String SERVICE_URI = "http://localhost:8080/b2s-rest/rest/banana/";
    static final String PATH_READ_FILE = "readFile";
    static final String PATH_CONVERT = "convert";
    private WebTarget tut;

    @Before
    public void init() {
        this.tut = ClientBuilder.newClient().register(MultiPartFeature.class).target(SERVICE_URI);
    }

    @Test
    public void readFile_csvFile_readsAllRecords() throws Exception {
        // arrange
        final File file = new File(getClass().getClassLoader().getResource("test-banana.csv").getFile());
        final FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", file);
        Response response = null;
        // act
        try (final FormDataMultiPart formDataMultiPart = new FormDataMultiPart()) {
            final MultiPart multiPart = formDataMultiPart.bodyPart(fileDataBodyPart);
            response = this.tut //
                .path(PATH_READ_FILE) //
                .request(MediaType.APPLICATION_JSON) //
                .post(Entity.entity(multiPart, multiPart.getMediaType()));
        }
        // assert
        assertThat(response.getStatus(), is(200));
        final List<BananaTransactionDto> result = response.readEntity(jsonBananaDtoConverter());
        assertThat(result, hasSize(11));
    }

    @Test
    public void convert_listBananaDto_createListSegaDto() throws Exception {
        //arrange
        List<BananaTransactionDto> bananaTransaction = null;
        final File file = new File(getClass().getClassLoader().getResource("test-banana.csv").getFile());
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            bananaTransaction = new BananaTransactionReader().readTransactions(reader);
        }
        Response response = null;
        //act
        response = this.tut //
            .path(PATH_CONVERT) //
            .request(MediaType.APPLICATION_JSON) //
            .post(Entity.json(bananaTransaction));
        //assert
        assertThat(response.getStatus(), is(200));
        final List<SegaDto> result = response.readEntity(jsonSegaDtoConverter());
        assertThat(result, hasSize(26));
    }

    private GenericType<List<BananaTransactionDto>> jsonBananaDtoConverter() {
        return new GenericType<List<BananaTransactionDto>>() {
            // Empty
        };
    }

    private GenericType<List<SegaDto>> jsonSegaDtoConverter() {
        return new GenericType<List<SegaDto>>() {
            // Empty
        };
    }
}
