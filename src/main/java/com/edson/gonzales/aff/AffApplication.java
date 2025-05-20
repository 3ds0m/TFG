package com.edson.gonzales.aff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AffApplication {

    public static void main(String[] args) {
        SpringApplication.run(AffApplication.class, args);
    }

}
