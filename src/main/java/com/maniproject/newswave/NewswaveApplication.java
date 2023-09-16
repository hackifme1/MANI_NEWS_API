package com.maniproject.newswave;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableCaching
@Slf4j
public class NewswaveApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(NewswaveApplication.class, args);

    }
}

