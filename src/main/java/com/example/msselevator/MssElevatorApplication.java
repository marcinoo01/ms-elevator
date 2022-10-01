package com.example.msselevator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableAsync
public class MssElevatorApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MssElevatorApplication.class, args);
    }

    @PostConstruct
    public void runner() {
    }
}
