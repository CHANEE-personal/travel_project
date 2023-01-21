package com.travel;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "com")
@EnableCaching
public class TravelAdminProject extends SpringBootServletInitializer {
    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.properties";

    public static void main(String[] args) {
        new SpringApplicationBuilder(TravelAdminProject.class).properties(APPLICATION_LOCATIONS).run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TravelAdminProject.class);
    }
}
