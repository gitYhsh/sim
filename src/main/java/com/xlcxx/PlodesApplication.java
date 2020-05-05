package com.xlcxx;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan(basePackages = {"com.xlcxx.plodes.busylogic.*.dao","com.xlcxx.plodes.system.*.dao"})
@EnableCaching
@EnableSwagger2
@EnableSwaggerBootstrapUI
@EnableScheduling
public class PlodesApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PlodesApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(PlodesApplication.class, args);
    }

}
