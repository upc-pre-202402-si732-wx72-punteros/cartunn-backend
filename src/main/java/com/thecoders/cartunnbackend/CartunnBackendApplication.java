package com.thecoders.cartunnbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class CartunnBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartunnBackendApplication.class, args);
    }

    @Configuration
    public static class Myconfiguration{
        @Bean
        public WebMvcConfigurer corsConfigurer() {
            return new WebMvcConfigurer() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/**")
                            .allowedOrigins("http://localhost:3306", "http://localhost:3306/","http://localhost:4200", "http://localhost:4200/", "https://cartunn-backend-production.up.railway.app", "https://cartunn-backend-production.up.railway.app/", "https://cartunn-backend-production.up.railway.app/swagger-ui/index.html#", "https://cartunn-backend-production.up.railway.app/swagger-ui/index.html#/")
                            .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
                            .allowedHeaders("*");
                }
            };
        }
    }
}