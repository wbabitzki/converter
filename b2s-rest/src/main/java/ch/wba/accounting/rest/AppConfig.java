package ch.wba.accounting.rest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

@ApplicationPath("rest")
public class AppConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<>(Arrays.asList( //
            CorsFilter.class, //
            BananaResource.class, //
            MultiPartFeature.class));
    }
}
