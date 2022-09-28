package com.example.msselevator;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@Log4j2
public class MssElevatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MssElevatorApplication.class, args);
    }

    @PostConstruct
    public void runner() {
    }
}
