package com.edson.gonzales.aff.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("https://edsontfg.vercel.app")); // ✅ Solo Vercel
        config.setAllowedMethods(Arrays.asList("GET", "POST")); // ✅ GET para obtener, POST para pago
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("*")); // Permitir todos los headers necesarios

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Aplica a toda la API

        return new CorsFilter(source);
    }
}
