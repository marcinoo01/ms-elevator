package com.example.msselevator.service;

import com.example.msselevator.domain.Elevator;
import com.example.msselevator.domain.ElevatorEvent;
import com.example.msselevator.domain.ElevatorState;
import com.example.msselevator.repository.ElevatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElevatorServiceImpl implements ElevatorService {

    private final ElevatorRepository elevatorRepository;
    private final ElevatorMachine elevatorMachine;

    @Value("${mss-elevator.request.sleep.seconds}")
    private Integer sleepTime;

    @Override
    public List<Elevator> status() {
        return elevatorRepository.findAll();
    }

    @Override
    public void call(Integer level) {
        final Elevator closestElevator = findClosestIdleElevator(level);
        final int differenceLevel = closestElevator.getCurrentLevel() - level;
        final StateMachine<ElevatorState, ElevatorEvent> stateMachine = elevatorMachine.build(closestElevator.getId());

        log.debug(format("Elevator id = %d : Closing door at level: %d\n Destination level: %d", closestElevator.getId(),
                closestElevator.getCurrentLevel(), level));

        closestElevator.setTargetLevel(level);
        elevatorMachine.sendEvent(closestElevator.getId(), stateMachine, ElevatorEvent.CALL_ELEVATOR);

        bringElevator(differenceLevel, closestElevator);
        elevatorMachine.sendEvent(closestElevator.getId(), stateMachine, ElevatorEvent.STOP);
        log.debug(format("Elevator id = %d : Opening door at requested level: %d", closestElevator.getId(), level));
    }


    @Override
    public void request(Integer id, Integer requestLevel) {
        final Elevator requestedElevator = findElevatorById(id);
        final int differenceLevel = requestedElevator.getCurrentLevel() - requestLevel;
        final StateMachine<ElevatorState, ElevatorEvent> stateMachine = elevatorMachine.build(id);

        log.debug(format("Elevator id = %d : Closing door at level: %d\n Destination level: %d", requestedElevator.getId(),
                requestedElevator.getCurrentLevel(), requestLevel));

        requestedElevator.setTargetLevel(requestLevel);
        elevatorMachine.sendEvent(id, stateMachine, ElevatorEvent.PICK_LEVEL);

        bringElevator(differenceLevel, requestedElevator);
        elevatorMachine.sendEvent(id, stateMachine, ElevatorEvent.STOP);
        log.debug(format("Elevator id = %d : Opening door at requested level: %d", requestedElevator.getId(), requestLevel));
    }

    private void bringElevator(Integer amountOfLevels, Elevator elevator) {
        if (amountOfLevels < 0) {
            callElevatorFromBottom(amountOfLevels, elevator);
        } else if (amountOfLevels > 0) {
            callElevatorFromTop(amountOfLevels, elevator);
        }
    }

    private void callElevatorFromBottom(Integer difference, Elevator elevator) {
        difference = Math.abs(difference);
        for (int i = 0; i < difference; i++) {
            step();
            elevator.setCurrentLevel(elevator.getCurrentLevel() + 1);
            elevatorRepository.save(elevator);
        }
    }

    private void callElevatorFromTop(Integer difference, Elevator elevator) {
        for (int i = 0; i < difference; i++) {
            step();
            elevator.setCurrentLevel(elevator.getCurrentLevel() - 1);
            elevatorRepository.save(elevator);
        }
    }

    private void step() {
        try {
            TimeUnit.SECONDS.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Elevator findClosestIdleElevator(Integer level) {
        final List<Elevator> idleElevator = findIdleElevator();
        final boolean elevatorExistOnSameLevel = checkIfElevatorExistsOnSame(level, idleElevator);

        if (elevatorExistOnSameLevel) {
            return findOneElevatorAtSpecific(level);
        } else {
            val ref = new Object() {
                int closestElevatorId = 0;
                int minDifference = Integer.MAX_VALUE;
            };
            idleElevator
                    .forEach(freeElevator -> {
                                int levelDifference = Math.abs(freeElevator.getCurrentLevel() - level);

                                if (Math.min(ref.minDifference, levelDifference) != ref.minDifference) {
                                    ref.closestElevatorId = freeElevator.getId();
                                    ref.minDifference = levelDifference;
                                }
                            }
                    );
            return findElevatorById(ref.closestElevatorId);
        }
    }

    private Elevator findElevatorById(Integer id) {
        return elevatorRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("Requested elevator with id: %d does not exist", id)));
    }

    private List<Elevator> findIdleElevator() {
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
