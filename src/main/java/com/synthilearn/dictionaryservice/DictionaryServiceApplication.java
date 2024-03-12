package com.synthilearn.dictionaryservice;

import com.synthilearn.loggingstarter.EnableLogging;
import com.synthilearn.securestarter.EnableTokenResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableTokenResolver
@EnableLogging
public class DictionaryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DictionaryServiceApplication.class, args);
    }

}
