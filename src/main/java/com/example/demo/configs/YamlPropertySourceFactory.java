package com.example.demo.configs;

import lombok.var;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;

public class YamlPropertySourceFactory implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        final var factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        final var properties = factory.getObject();
        return new PropertiesPropertySource(resource.getResource().getFilename(),properties);
    }
}
