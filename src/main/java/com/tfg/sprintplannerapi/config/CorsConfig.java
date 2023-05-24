package com.tfg.sprintplannerapi.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CorsConfig {
   /* @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        List<String> exposes = new ArrayList<>();
        exposes.add("set-cooke");
        exposes.add("x-csrf-token");
        exposes.add("z-http-method");
        config.setExposedHeaders(exposes);
        config.addAllowedHeader("setcookie");
        config.addAllowedHeader("origin");
        config.addAllowedHeader("maxdataserviceversion");
        config.addAllowedHeader("x-csrf-token");
        config.addAllowedHeader("dataserviceversion");
        config.addAllowedHeader("x-httpmethod");
        config.addAllowedHeader("X-Requested-With");
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Accept");
        config.addAllowedHeader("uid");
        config.addAllowedHeader("jwt");
        config.addAllowedHeader("apikey");
        config.addAllowedHeader("apiCache");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;

    }*/
}
