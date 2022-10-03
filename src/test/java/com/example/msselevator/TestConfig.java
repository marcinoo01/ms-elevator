package com.example.msselevator;

import com.example.msselevator.domain.ElevatorEvent;
import com.example.msselevator.domain.ElevatorState;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineConfigBuilder;

@TestConfiguration
public class TestConfig {

    @Bean
    public StateMachineFactory<ElevatorState, ElevatorEvent> factory(){
        return StateMachineFactory.create(new StateMachineConfigBuilder<>());
    }
}
