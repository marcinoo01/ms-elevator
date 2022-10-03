package com.example.msselevator.util;

import com.example.msselevator.domain.Elevator;
import com.example.msselevator.domain.ElevatorState;

public final class MockUtil {

    private MockUtil() {

    }

    public final static Elevator IDLE_ELEVATOR_LEVEL_0 = Elevator.builder().id(0).state(ElevatorState.IDLE).targetLevel(null).currentLevel(0).build();
    public final static Elevator IDLE_ELEVATOR_LEVEL_3 = Elevator.builder().id(2).state(ElevatorState.IDLE).targetLevel(null).currentLevel(3).build();
    public final static Elevator IDLE_ELEVATOR_LEVEL_5 = Elevator.builder().id(3).state(ElevatorState.IDLE).targetLevel(null).currentLevel(5).build();

}
