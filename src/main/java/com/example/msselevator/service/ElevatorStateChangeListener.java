package com.example.msselevator.service;

import com.example.msselevator.domain.Elevator;
import com.example.msselevator.domain.ElevatorEvent;
import com.example.msselevator.domain.ElevatorState;
import com.example.msselevator.repository.ElevatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ElevatorStateChangeListener extends StateMachineInterceptorAdapter<ElevatorState, ElevatorEvent> {
    private final ElevatorRepository elevatorRepository;

    @Override
    public void preStateChange(State<ElevatorState, ElevatorEvent> state, Message<ElevatorEvent> message, Transition<ElevatorState, ElevatorEvent> transition, StateMachine<ElevatorState, ElevatorEvent> stateMachine, StateMachine<ElevatorState, ElevatorEvent> rootStateMachine) {
        Optional.ofNullable(message)
                .flatMap(msg -> Optional.ofNullable((Integer) msg.getHeaders()
                        .getOrDefault(ElevatorServiceImpl.ELEVATOR_ID_HEADER, -1))).ifPresent(id -> {
                            Elevator elevator = elevatorRepository.getReferenceById(id);
                            elevator.setState(state.getId());
                            elevatorRepository.save(elevator);
                        }
                );
    }
}
