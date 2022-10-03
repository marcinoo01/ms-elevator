package com.example.msselevator.service;

import com.example.msselevator.domain.ElevatorEvent;
import com.example.msselevator.domain.ElevatorState;
import org.springframework.statemachine.StateMachine;

public interface ElevatorMachine {

    StateMachine<ElevatorState, ElevatorEvent> build(Integer elevatorId);

    void sendEvent(Integer elevatorId, StateMachine<ElevatorState, ElevatorEvent> stateMachine, ElevatorEvent event);
}
