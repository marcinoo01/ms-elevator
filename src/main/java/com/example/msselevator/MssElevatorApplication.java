package com.example.msselevator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class MssElevatorApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MssElevatorApplication.class, args);
    }

    @PostConstruct
    public void runner() {
    }
}
