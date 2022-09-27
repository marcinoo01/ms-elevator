package com.example.msselevator.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class Elevator {
    private Integer id;
    private Integer level;
    private Set<Integer> targetLevels;
    private ElevatorState state;
}