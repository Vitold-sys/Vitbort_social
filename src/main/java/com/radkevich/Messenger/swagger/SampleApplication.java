package com.radkevich.Messenger.swagger;

import io.swagger.jaxrs.config.BeanConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class SampleApplication extends Application {
    public SampleApplication() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setBasePath("/api");
        beanConfig.setResourcePackage("com.radkevich");
        beanConfig.setScan(true);
    }
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet();
        resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        return resources;
    }
}