package com.example.msselevator.service;

import com.example.msselevator.domain.Elevator;
import com.example.msselevator.domain.ElevatorState;
import com.example.msselevator.repository.ElevatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ElevatorServiceImpl implements ElevatorService {

    private final ElevatorRepository elevatorRepository;

    @Override
    public List<Elevator> status() {
        return elevatorRepository.findAll();
    }

    @Override
    public void step() {

    }

    @Override
    public void call(Integer level) {
        Elevator closestElevator = invokeClosestElevator(level);
    }

    private Elevator invokeClosestElevator(Integer level) {

        final List<Elevator> freeElevators = findFreeElevator();
        final boolean elevatorExistOnSameLevel = freeElevators
                .stream()
                .anyMatch(elevator -> Objects.equals(elevator.getLevel(), level)
                );
        Elevator elevator;

        if (elevatorExistOnSameLevel) {
            elevator = elevatorRepository.findByLevel(level);
        } else {
             val ref = new Object() {
                int closestElevatorId = 0;
            };
            freeElevators
                    .forEach(freeElevator -> {
                                int minDifference = Integer.MAX_VALUE;
                                if (Math.min(minDifference, Math.abs(freeElevator.getLevel() - level)) != minDifference) {
                                    ref.closestElevatorId = freeElevator.getId();
                                }
                            }
                    );
            elevator = elevatorRepository.findByLevel(ref.closestElevatorId);
        }
        return elevator;
    }

    private List<Elevator> findFreeElevator() {
        return elevatorRepository
                .findAll()
                .stream()
                .filter(elevator -> elevator.getState() == ElevatorState.IDLE)
                .toList();
    }
}
