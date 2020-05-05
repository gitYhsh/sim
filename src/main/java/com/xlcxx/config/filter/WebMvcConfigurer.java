package com.xlcxx.config.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


/**
 * 创建时间：2018年5月19日 上午11:50:27
 * 项目名称：taskmanager
 *
 * @author yhsh
 * @version 1.0
 * @since JDK 1.7.0_21
 * 类说明：  拦截器
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurationSupport {

    @Bean
    public AutonInterceptor getTokenInterceptor(){
        return new AutonInterceptor();
    }

        @Override
        protected void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(getTokenInterceptor());
        }
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                    .maxAge(3600)
                    .allowCredentials(true);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");

    }

}

