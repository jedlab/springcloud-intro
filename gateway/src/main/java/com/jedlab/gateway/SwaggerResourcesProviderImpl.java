package com.jedlab.gateway;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Primary
@EnableAutoConfiguration
public class SwaggerResourcesProviderImpl implements SwaggerResourcesProvider
{

    @Override
    public List<SwaggerResource> get()
    {
        List<SwaggerResource> resources = new ArrayList<>();
        resources.add(swaggerResource("user-service", "/user/v2/api-docs", "0.0.1-SNAPSHOT"));
        resources.add(swaggerResource("payment-service", "/payment/v2/api-docs", "0.0.1-SNAPSHOT"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version)
    {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }

    @Bean
    public Docket swaggerApi()
    {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage(GatewayApplication.class.getPackage().getName())).paths(PathSelectors.any()).build()
                .directModelSubstitute(LocalDate.class, String.class).genericModelSubstitutes(new Class[] { ResponseEntity.class });
    }

}
