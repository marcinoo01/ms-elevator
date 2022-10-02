package com.example.msselevator.service;

import com.example.msselevator.domain.Elevator;
import com.example.msselevator.domain.ElevatorEvent;
import com.example.msselevator.domain.ElevatorState;
import com.example.msselevator.repository.ElevatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
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
    public static final String ELEVATOR_ID_HEADER = "elevator_id";

    private final ElevatorRepository elevatorRepository;
    private final StateMachineFactory<ElevatorState, ElevatorEvent> stateMachineFactory;
    private final ElevatorStateChangeListener elevatorStateChangeListener;

    @Override
    public List<Elevator> status() {
        return elevatorRepository.findAll();
    }

    @Override
    public void call(Integer level) {
        final Elevator closestElevator = findClosestIdleElevator(level);
        log.debug(String.format("Closing door at level: %d\n Destination level: %d", closestElevator.getCurrentLevel(), level));

        final int differenceLevel = closestElevator.getCurrentLevel() - level;
        closestElevator.setTargetLevel(level);

        final StateMachine<ElevatorState, ElevatorEvent> stateMachine = build(closestElevator.getId());
        sendEvent(closestElevator.getId(), stateMachine, ElevatorEvent.CALL_ELEVATOR);
        elevatorRepository.save(closestElevator);

        bringElevator(differenceLevel, closestElevator);
        sendEvent(closestElevator.getId(), stateMachine, ElevatorEvent.STOP);
        log.debug(String.format("Opening door at requested level: %d", level));

        elevatorRepository.save(closestElevator);
    }


    @Override
    public void request(Integer id, Integer requestLevel) {
        final Elevator requestedElevator = elevatorRepository.findById(id).orElseThrow(NoSuchElementException::new);

        requestedElevator.setTargetLevel(requestLevel);
        final StateMachine<ElevatorState, ElevatorEvent> stateMachine = build(id);
        sendEvent(id, stateMachine, ElevatorEvent.PICK_LEVEL);

        elevatorRepository.save(requestedElevator);
        final int currentLevel = requestedElevator.getCurrentLevel();
        final int differenceLevel = currentLevel - requestLevel;

        log.debug(String.format("Closing door at level: %d\n Destination level: %d", currentLevel, requestLevel));
        bringElevator(differenceLevel, requestedElevator);
        log.debug(String.format("Opening door at requested level: %d", requestLevel));

        sendEvent(id, stateMachine, ElevatorEvent.STOP);
        elevatorRepository.save(requestedElevator);
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
            TimeUnit.SECONDS.sleep(LEVEL_CHANGE_SECONDS_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Elevator findClosestIdleElevator(Integer level) {

        final List<Elevator> freeElevators = findFreeElevator();
        final boolean elevatorExistOnSameLevel = checkIfElevatorExistsOnSame(level, freeElevators);
        if (elevatorExistOnSameLevel) {
            return findOneElevatorAtSpecific(level);
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
            return elevatorRepository.findById(ref.closestElevatorId).orElseThrow(NoSuchElementException::new);
        }
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

    private void sendEvent(Integer elevatorId, StateMachine<ElevatorState, ElevatorEvent> stateMachine, ElevatorEvent event) {
        Message<ElevatorEvent> message = MessageBuilder.withPayload(event)
                .setHeader(ELEVATOR_ID_HEADER, elevatorId)
                .build();
        stateMachine.sendEvent(message);
    }

    private StateMachine<ElevatorState, ElevatorEvent> build(Integer elevatorId) {
        Elevator elevator = elevatorRepository.getReferenceById(elevatorId);
        StateMachine<ElevatorState, ElevatorEvent> stateMachine = stateMachineFactory
                .getStateMachine(Integer.toString(elevator.getId()));
        stateMachine.stop();
        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(elevatorStateChangeListener);
                    sma.resetStateMachine(new DefaultStateMachineContext<>(elevator.getState(), null, null, null));
                });
        stateMachine.start();
        return stateMachine;
    }
}
