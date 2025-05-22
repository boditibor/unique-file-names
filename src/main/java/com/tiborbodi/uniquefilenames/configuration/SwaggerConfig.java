package com.tiborbodi.uniquefilenames.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration for API documentation.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configures the OpenAPI bean for Swagger UI.
     *
     * @return the OpenAPI configuration
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Unique File Names API")
                        .version("1.0")
                        .description("API documentation for managing unique file names"));
    }

}

