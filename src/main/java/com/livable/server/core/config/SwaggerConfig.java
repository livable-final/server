package com.livable.server.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    private static final String SERVICE_NAME = "오피스너 - 식스센스";
    private static final String API_VERSION = "V1";
    private static final String API_DESCRIPTION = "오피스너";
    private static final String API_URL = "https://livableserver.site/";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.livable.server"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(SERVICE_NAME)
                .version(API_VERSION)
                .description(API_DESCRIPTION)
                .termsOfServiceUrl(API_URL)
                .build();
    }
}
