package com.example.msselevator.config;

import com.example.msselevator.domain.Elevator;
import com.example.msselevator.domain.ElevatorState;
import com.example.msselevator.repository.ElevatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final ElevatorRepository elevatorRepository;

    @PostConstruct
    public void runner() {
        Elevator elevator1 = Elevator.builder().currentLevel(0).targetLevel(null).state(ElevatorState.IDLE).build();
        Elevator elevator2 = Elevator.builder().currentLevel(0).targetLevel(null).state(ElevatorState.IDLE).build();
        Elevator elevator3 = Elevator.builder().currentLevel(0).targetLevel(null).state(ElevatorState.IDLE).build();
        Elevator elevator4 = Elevator.builder().currentLevel(0).targetLevel(null).state(ElevatorState.IDLE).build();
        Elevator elevator5 = Elevator.builder().currentLevel(0).targetLevel(null).state(ElevatorState.IDLE).build();
        Elevator elevator6 = Elevator.builder().currentLevel(0).targetLevel(null).state(ElevatorState.IDLE).build();
        Elevator elevator7 = Elevator.builder().currentLevel(0).targetLevel(null).state(ElevatorState.IDLE).build();
        Elevator elevator8 = Elevator.builder().currentLevel(0).targetLevel(null).state(ElevatorState.IDLE).build();
        Elevator elevator9 = Elevator.builder().currentLevel(0).targetLevel(null).state(ElevatorState.IDLE).build();
        Elevator elevator10 = Elevator.builder().currentLevel(0).targetLevel(null).state(ElevatorState.IDLE).build();
        Elevator elevator11 = Elevator.builder().currentLevel(0).targetLevel(null).state(ElevatorState.IDLE).build();
        Elevator elevator12 = Elevator.builder().currentLevel(0).targetLevel(null).state(ElevatorState.IDLE).build();
        Elevator elevator13 = Elevator.builder().currentLevel(0).targetLevel(null).state(ElevatorState.IDLE).build();
        Elevator elevator14 = Elevator.builder().currentLevel(0).targetLevel(null).state(ElevatorState.IDLE).build();
        Elevator elevator15 = Elevator.builder().currentLevel(0).targetLevel(null).state(ElevatorState.IDLE).build();
        Elevator elevator16 = Elevator.builder().currentLevel(0).targetLevel(null).state(ElevatorState.IDLE).build();
        elevatorRepository.saveAll(List.of(elevator2, elevator3, elevator4, elevator5, elevator6, elevator1, elevator11, elevator12, elevator13, elevator14, elevator15, elevator16, elevator7, elevator8, elevator9, elevator10));
    }
}
