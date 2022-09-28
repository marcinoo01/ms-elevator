package com.example.msselevator.config;

import com.example.msselevator.domain.ElevatorEvent;
import com.example.msselevator.domain.ElevatorState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

@SpringBootTest
class StateMachineConfigTest {

    @Autowired
    private StateMachineFactory<ElevatorState, ElevatorEvent> factory;

    @Test
    void testMachineStates() {
        StateMachine<ElevatorState, ElevatorEvent> machine = factory.getStateMachine(UUID.randomUUID());
        machine.start();
        machine.sendEvent(ElevatorEvent.PICK_LEVEL);
        machine.sendEvent(ElevatorEvent.CALL_ELEVATOR);
        machine.sendEvent(ElevatorEvent.STOP);
        machine.sendEvent(ElevatorEvent.CALL_ELEVATOR);
    }

}