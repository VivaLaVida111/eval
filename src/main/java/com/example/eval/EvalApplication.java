package com.example.eval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EvalApplication {

    public static void main(String[] args) {
        System.setProperty("user.timezone","GMT +08");
        SpringApplication.run(EvalApplication.class, args);
    }

}
