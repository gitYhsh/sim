package com.xlcxx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class APiConfig {

    @Bean
    public Docket createRWEbtApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("目标2.0 - 基本接口文档")
                .apiInfo(apiInfo())
                .select()
                .apis( RequestHandlerSelectors.basePackage("com.xlcxx.web.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("山东教育免认证api文档")
                .description("免认证前端接口文档")
                .version("1.0")
                .build();
    }
}