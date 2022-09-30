package com.example.msselevator.service;

import com.example.msselevator.domain.Elevator;

import java.util.List;

public interface ElevatorService {
    List<Elevator> status();

    void call(Integer level);
}
