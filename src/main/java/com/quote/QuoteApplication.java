package com.quote;

import com.quote.service.init.InitUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class QuoteApplication extends SpringBootServletInitializer implements CommandLineRunner {

    @Autowired
    private InitUserService initUserService;

    public static void main(String[] args) {
        SpringApplication.run(QuoteApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(QuoteApplication.class);
    }

    @Override
    public void run(String... args) {
        initUserService.init();
    }

}
