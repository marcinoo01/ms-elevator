package com.example.msselevator;

import com.example.msselevator.domain.Elevator;
import com.example.msselevator.domain.ElevatorState;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Set;

@SpringBootApplication
@Log4j2
public class MssElevatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MssElevatorApplication.class, args);
    }

    @PostConstruct
    public void runner() {
        Elevator elevator1 = Elevator.builder().id(1).level(0).targetLevels(Set.of()).state(ElevatorState.IDLE).build();
        Elevator elevator2 = Elevator.builder().id(2).level(0).targetLevels(Set.of()).state(ElevatorState.IDLE).build();
        Elevator elevator3 = Elevator.builder().id(3).level(0).targetLevels(Set.of()).state(ElevatorState.IDLE).build();
        Elevator elevator4 = Elevator.builder().id(4).level(0).targetLevels(Set.of()).state(ElevatorState.IDLE).build();
        Elevator elevator5 = Elevator.builder().id(5).level(0).targetLevels(Set.of()).state(ElevatorState.IDLE).build();
        Elevator elevator6 = Elevator.builder().id(6).level(0).targetLevels(Set.of()).state(ElevatorState.IDLE).build();
        Elevator elevator7 = Elevator.builder().id(7).level(0).targetLevels(Set.of()).state(ElevatorState.IDLE).build();
        Elevator elevator8 = Elevator.builder().id(8).level(0).targetLevels(Set.of()).state(ElevatorState.IDLE).build();
        Elevator elevator9 = Elevator.builder().id(9).level(0).targetLevels(Set.of()).state(ElevatorState.IDLE).build();
        Elevator elevator10 = Elevator.builder().id(10).level(0).targetLevels(Set.of()).state(ElevatorState.IDLE).build();
        Elevator elevator11 = Elevator.builder().id(11).level(0).targetLevels(Set.of()).state(ElevatorState.IDLE).build();
        Elevator elevator12 = Elevator.builder().id(12).level(0).targetLevels(Set.of()).state(ElevatorState.IDLE).build();
        Elevator elevator13 = Elevator.builder().id(13).level(0).targetLevels(Set.of()).state(ElevatorState.IDLE).build();
        Elevator elevator14 = Elevator.builder().id(14).level(0).targetLevels(Set.of()).state(ElevatorState.IDLE).build();
        Elevator elevator15 = Elevator.builder().id(15).level(0).targetLevels(Set.of()).state(ElevatorState.IDLE).build();
        Elevator elevator16 = Elevator.builder().id(16).level(0).targetLevels(Set.of()).state(ElevatorState.IDLE).build();
    }
}
