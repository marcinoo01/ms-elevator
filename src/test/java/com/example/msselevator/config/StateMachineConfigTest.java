package com.example.msselevator.config;

import com.example.msselevator.domain.ElevatorEvent;
import com.example.msselevator.domain.ElevatorState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StateMachineConfigTest {

    @Autowired
    private StateMachineFactory<ElevatorState, ElevatorEvent> factory;

    @Test
    void testMachineStates() {
        final StateMachine<ElevatorState, ElevatorEvent> machine = factory.getStateMachine(UUID.randomUUID());
        machine.start();
        machine.sendEvent(ElevatorEvent.PICK_LEVEL);
        assertEquals(ElevatorState.DELIVERING, machine.getState().getId());
        machine.sendEvent(ElevatorEvent.STOP);
        assertEquals(ElevatorState.IDLE, machine.getState().getId());
        machine.sendEvent(ElevatorEvent.CALL_ELEVATOR);
        assertEquals(ElevatorState.REQUESTED, machine.getState().getId());
        machine.sendEvent(ElevatorEvent.STOP);
        assertEquals(ElevatorState.IDLE, machine.getState().getId());
    }

    @Test
    void testInvalidOperation() {
        final StateMachine<ElevatorState, ElevatorEvent> machine = factory.getStateMachine(UUID.randomUUID());
        machine.start();
        machine.sendEvent(ElevatorEvent.PICK_LEVEL);
        assertEquals(ElevatorState.DELIVERING, machine.getState().getId());
        machine.sendEvent(ElevatorEvent.CALL_ELEVATOR);
        assertEquals(ElevatorState.DELIVERING, machine.getState().getId());
        machine.sendEvent(ElevatorEvent.STOP);
        assertEquals(ElevatorState.IDLE, machine.getState().getId());
        machine.sendEvent(ElevatorEvent.CALL_ELEVATOR);
        assertEquals(ElevatorState.REQUESTED, machine.getState().getId());
        machine.sendEvent(ElevatorEvent.PICK_LEVEL);
        assertEquals(ElevatorState.REQUESTED, machine.getState().getId());
    }

}