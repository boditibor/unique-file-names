package com.tiborbodi.uniquefilenames.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration for resource handling, including Javadoc documentation exposure.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Adds resource handlers for serving static resources such as Javadoc HTML files.
     *
     * @param registry the resource handler registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/v1/doc/**")
                .addResourceLocations("classpath:/docs/javadoc/", "file:build/docs/javadoc/");
    }
}

