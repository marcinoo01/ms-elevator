package com.example.msselevator.config;

import com.example.msselevator.domain.ElevatorEvent;
import com.example.msselevator.domain.ElevatorState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Slf4j
@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends StateMachineConfigurerAdapter<ElevatorState, ElevatorEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<ElevatorState, ElevatorEvent> states) throws Exception {
        states
                .withStates()
                .initial(ElevatorState.IDLE)
                .states(EnumSet.allOf(ElevatorState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<ElevatorState, ElevatorEvent> transitions) throws Exception {
        transitions
                .withExternal().source(ElevatorState.IDLE).target(ElevatorState.REQUESTED).event(ElevatorEvent.CALL_ELEVATOR)
                .and()
                .withExternal().source(ElevatorState.IDLE).target(ElevatorState.DELIVERING).event(ElevatorEvent.PICK_LEVEL)
                .and()
                .withExternal().source(ElevatorState.REQUESTED).target(ElevatorState.IDLE).event(ElevatorEvent.STOP)
                .and()
                .withExternal().source(ElevatorState.DELIVERING).target(ElevatorState.IDLE).event(ElevatorEvent.STOP);
    }
}
