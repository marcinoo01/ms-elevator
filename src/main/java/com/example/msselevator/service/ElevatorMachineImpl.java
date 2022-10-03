package com.example.msselevator.service;

import com.example.msselevator.domain.Elevator;
import com.example.msselevator.domain.ElevatorEvent;
import com.example.msselevator.domain.ElevatorState;
import com.example.msselevator.repository.ElevatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ElevatorMachineImpl implements ElevatorMachine {

    public static final String ELEVATOR_ID_HEADER = "elevator_id";

    private final ElevatorRepository elevatorRepository;
    private final StateMachineFactory<ElevatorState, ElevatorEvent> stateMachineFactory;
    private final ElevatorStateInterceptor elevatorStateChangeListener;

    @Override
    public StateMachine<ElevatorState, ElevatorEvent> build(Integer elevatorId) {
        final Elevator elevator = elevatorRepository.getReferenceById(elevatorId);
        final StateMachine<ElevatorState, ElevatorEvent> stateMachine = stateMachineFactory
                .getStateMachine(Integer.toString(elevator.getId()));

        stateMachine.stop();
        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(elevatorStateChangeListener);
                    sma.resetStateMachine(new DefaultStateMachineContext<>
                            (elevator.getState(), null, null, null));
                });
        stateMachine.start();

        return stateMachine;
    }

    @Override
    public void sendEvent(Integer elevatorId, StateMachine<ElevatorState, ElevatorEvent> stateMachine, ElevatorEvent event) {
        final Message<ElevatorEvent> message = MessageBuilder.withPayload(event)
                .setHeader(ELEVATOR_ID_HEADER, elevatorId)
                .build();
        stateMachine.sendEvent(message);
    }
}
