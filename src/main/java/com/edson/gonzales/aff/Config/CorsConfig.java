package com.edson.gonzales.aff.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*") // Durante el desarrollo permite pero luego poner un https://frontend.com
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Metodos que permite mi back
                        .allowCredentials(true);
            }
        };
    }
}
