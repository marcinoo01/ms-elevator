package com.example.msselevator.service;

import com.example.msselevator.domain.Elevator;
import com.example.msselevator.domain.ElevatorState;
import com.example.msselevator.repository.ElevatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElevatorServiceImpl implements ElevatorService {

    private static final Integer LEVEL_CHANGE_SECONDS_TIME = 5;

    private final ElevatorRepository elevatorRepository;

    @Override
    public List<Elevator> status() {
        return elevatorRepository.findAll();
    }

    @Override
    public void call(Integer level) {
        Elevator closestElevator = findClosestIdleElevator(level);
        closestElevator.setTargetLevel(level);
        int differenceLevel = closestElevator.getCurrentLevel() - level;

        if (differenceLevel < 0) {
            callElevatorFromBottom(differenceLevel, closestElevator);
        } else if (differenceLevel > 0) {
            callElevatorFromTop(differenceLevel, closestElevator);
        }
        log.debug("Opening door at requested level: " + level);
    }

    private void callElevatorFromBottom(Integer difference, Elevator elevator) {
        for (int i = 0; i < difference; i++) {
            step();
            elevator.setCurrentLevel(elevator.getCurrentLevel() + 1);
        }
    }

    private void callElevatorFromTop(Integer difference, Elevator elevator) {
        for (int i = 0; i < difference; i++) {
            step();
            elevator.setCurrentLevel(elevator.getCurrentLevel() - 1);
        }
    }

    private void step() {
        try {
            TimeUnit.SECONDS.sleep(LEVEL_CHANGE_SECONDS_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Elevator findClosestIdleElevator(Integer level) {

        final List<Elevator> freeElevators = findFreeElevator();
        final boolean elevatorExistOnSameLevel = checkIfElevatorExistsOnSame(level, freeElevators);
        Elevator elevator;

        if (elevatorExistOnSameLevel) {
            elevator = findOneElevatorAtSpecific(level);
        } else {
            val ref = new Object() {
                int closestElevatorId = 0;
                int minDifference = Integer.MAX_VALUE;
            };
            freeElevators
                    .forEach(freeElevator -> {
                                int levelDifference = Math.abs(freeElevator.getCurrentLevel() - level);

                                if (Math.min(ref.minDifference, levelDifference) != ref.minDifference) {
                                    ref.closestElevatorId = freeElevator.getId();
                                    ref.minDifference = levelDifference;
                                }
                            }
                    );
            elevator = elevatorRepository.findById(ref.closestElevatorId).orElseThrow(NoSuchElementException::new);
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

    private boolean checkIfElevatorExistsOnSame(Integer level, List<Elevator> freeElevators) {
        return freeElevators
                .stream()
                .anyMatch(elevator -> Objects.equals(elevator.getCurrentLevel(), level));
    }

    private Elevator findOneElevatorAtSpecific(Integer level) {
        return elevatorRepository
                .findByCurrentLevel(level)
                .stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
