package com.thecoders.cartunnbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaAuditing
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
                             .allowedOrigins("*")
                             .allowedMethods("*")
                             .allowedHeaders("*");
                 }
             };
         }
     }

     @EnableWebSecurity
     public class WebSecurityConfig {
         @Bean
         public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
             http.cors().and()
                 .csrf().disable();
             return http.build();
         }

         @Bean
         public CorsConfigurationSource corsConfigurationSource() {
             CorsConfiguration configuration = new CorsConfiguration();
             configuration.setAllowedOrigins(Arrays.asList("*"));
             configuration.setAllowedMethods(Arrays.asList("*"));
             configuration.setAllowedHeaders(Arrays.asList("*"));
             UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
             source.registerCorsConfiguration("/**", configuration);
             return source;
         }
     }
}