package com.paydaybank.dashboard.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.any;

@Configuration
@EnableSwagger2
public class ApplicationSwaggerConfig extends WebMvcConfigurationSupport {

    private final String WEB_PATH = "com.paydaybank.dashboard.web.api";

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(WEB_PATH))
                .paths(any())
                .build()
                .apiInfo(metaData())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .useDefaultResponseMessages(false);

    }
    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Payday Bank - Product Dashboard - REST API")
                .description("This project is developed to manage products on a dashboard.")
                .version("1.0.0")
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
                .contact(new Contact("Berkay Çelik", "https://berkaycelik.com.tr", "berkayc78@gmail.com"))
                .build();
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT TOKEN", "Authorization", "header");
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("JWT TOKEN", authorizationScopes));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("^((?!auth\\/(login|signup)).)*$"))
                .build();
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .scopeSeparator(",")
                .additionalQueryStringParams(null)
                .useBasicAuthenticationWithAccessCodeGrant(false)
                .build();
    }
}
