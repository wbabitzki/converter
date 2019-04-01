package ch.wba.accounting.rest;

import javax.ws.rs.ext.ContextResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonObjectMapperProvider implements ContextResolver<ObjectMapper> {
    private final ObjectMapper defaultObjectMapper;

    public JacksonObjectMapperProvider() {
        defaultObjectMapper = new DefaultObjectMapper();
    }

    @Override
    public ObjectMapper getContext(final Class<?> type) {
        return defaultObjectMapper;
    }
}
