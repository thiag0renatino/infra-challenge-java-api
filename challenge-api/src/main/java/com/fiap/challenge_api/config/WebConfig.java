package com.fiap.challenge_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.origin-patterns}")
    private String corsOriginPatterns;

    @Value("${cors.allowed-methods}")
    private String allowedMethods;

    @Value("${cors.allowed-headers:Authorization,Content-Type,Accept,Origin,User-Agent,Cache-Control,Pragma}")
    private String allowedHeaders;

    @Value("${cors.exposed-headers:Authorization,Location}")
    private String exposedHeaders;

    @Value("${cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Value("${cors.max-age:3600}")
    private long maxAge;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(corsOriginPatterns.split("\\s*,\\s*"))
                .allowedMethods(allowedMethods.split("\\s*,\\s*"))
                .allowedHeaders(allowedHeaders.split("\\s*,\\s*"))
                .exposedHeaders(exposedHeaders.split("\\s*,\\s*"))
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);
    }
}
