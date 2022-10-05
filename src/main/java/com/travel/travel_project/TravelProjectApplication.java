package com.travel.travel_project;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@ComponentScans({
        @ComponentScan(basePackages = "com.travel")
})
@SpringBootApplication(scanBasePackages = "com")
public class TravelProjectApplication extends SpringBootServletInitializer {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.properties";

    public static void main(String[] args) {
        new SpringApplicationBuilder(TravelProjectApplication.class).properties(APPLICATION_LOCATIONS).run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TravelProjectApplication.class);
    }

}
