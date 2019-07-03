package ch.wba.accounting.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataParam;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.banana.BananaTransactionReader;
import ch.wba.accounting.banana.validation.BananaValidator;
import ch.wba.accounting.banana.validation.BananaViolation;
import ch.wba.accounting.sega.ConverterService;
import ch.wba.accounting.sega.SegaDto;

@Stateless
@Path("banana")
public class BananaResource {
    private static final String PATH_READ_FILE = "readFile";
    private static final String PATH_CONVERT = "convert";
    private static final String PATH_VALIDATE = "validate";
    private BananaTransactionReader bananaReader;
    private ConverterService converterService;
    private BananaValidator validator;

    @Inject
    public void setBananaReader(final BananaTransactionReader bananaReader) {
        this.bananaReader = bananaReader;
    }

    @Inject
    public void setConverterService(final ConverterService converterService) {
        this.converterService = converterService;
    }

    @Inject
    public void setConverterService(final BananaValidator validator) {
        this.validator = validator;
    }

    @Path(PATH_READ_FILE)
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public List<BananaTransactionDto> readFile(@FormDataParam("file") final InputStream inputStream) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            return bananaReader.readTransactions(reader);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Path(PATH_CONVERT)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<SegaDto> convert(final List<BananaTransactionDto> bananaTransations) {
        return converterService.convert(bananaTransations);
    }

    @Path(PATH_VALIDATE)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<UUID, List<BananaViolation>> validate(final List<BananaTransactionDto> bananaTransations) {
        return validator.validate(bananaTransations);
    }
}
