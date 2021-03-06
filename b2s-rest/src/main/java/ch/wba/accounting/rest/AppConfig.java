package ch.wba.accounting.rest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("rest")
public class AppConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<>(Arrays.asList( //
            JacksonFeature.class, //
            CorsFilter.class, //
            BananaResource.class, //
            MultiPartFeature.class, //
            JacksonObjectMapperProvider.class, //
            RuntimeExceptionMapper.class));
    }
}
