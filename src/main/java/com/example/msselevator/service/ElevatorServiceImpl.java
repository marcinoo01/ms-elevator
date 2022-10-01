package com.example.msselevator.service;

import com.example.msselevator.domain.Elevator;
import com.example.msselevator.domain.ElevatorState;
import com.example.msselevator.repository.ElevatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElevatorServiceImpl implements ElevatorService {

    private static final Integer LEVEL_CHANGE_SECONDS_TIME = 5;

    private final ElevatorRepository elevatorRepository;

    @Override
    public Flux<Elevator> status() {
        return elevatorRepository.findAll();
    }

    @Override
    public Mono<Elevator> call(Integer level) {
        return findClosestIdleElevator(level)
                .flatMap(closestElevator -> {
                            closestElevator.setTargetLevel(level);
                            int differenceLevel = closestElevator.getCurrentLevel() - level;
                            bringElevator(differenceLevel, closestElevator);
                            log.debug(String.format("Elevator id: %s Opening door at requested level: %s", closestElevator.getId(), level));
                            return elevatorRepository.save(closestElevator);
                        }
                );
    }

    @Override
    public Mono<Elevator> request(String id, Integer requestLevel) {
        return elevatorRepository.findById(id)
                .flatMap(elevator -> {
                            elevator.setTargetLevel(requestLevel);
                            Integer currentLevel = elevator.getTargetLevel();
                            Integer differenceLevel = currentLevel - requestLevel;
                            log.debug(String.format("Elevator id: %s Closing door at level: currentLevel \nDestination level: %s", elevator.getId(), elevator.getTargetLevel()));
                            bringElevator(differenceLevel, elevator);
                            return elevatorRepository.save(elevator);
                        }
                );
    }

    @Override
    public Flux<Elevator> pickOne() {
        return elevatorRepository
                .findAll()
                .filter(elevator -> elevator.getState() == ElevatorState.IDLE);
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
            final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            ScheduledFuture<?> future = service.schedule(() ->
                    elevator.setCurrentLevel(elevator.getCurrentLevel() + 1), LEVEL_CHANGE_SECONDS_TIME, TimeUnit.SECONDS
            );
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void callElevatorFromTop(Integer difference, Elevator elevator) {
        for (int i = 0; i < difference; i++) {
            final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            service.schedule(() -> elevator.setCurrentLevel(elevator.getCurrentLevel() - 1), LEVEL_CHANGE_SECONDS_TIME, TimeUnit.SECONDS);
        }
    }

    private Mono<Elevator> findClosestIdleElevator(Integer level) {
        return elevatorRepository
                .findAll()
                .filter(elevator -> elevator.getState() == ElevatorState.IDLE)
                .flatMap(elevator -> {
                    val ref = new Object() {
                        String closestElevatorId = null;
                        int minDifference = Integer.MAX_VALUE;
                    };
                    int levelDifference = Math.abs(elevator.getCurrentLevel() - level);

                    if (Math.min(ref.minDifference, levelDifference) != ref.minDifference) {
                        ref.closestElevatorId = elevator.getId();
                        ref.minDifference = levelDifference;
                    }
                    return elevatorRepository.findById(ref.closestElevatorId);
                })
                .next();
//        final boolean elevatorExistOnSameLevel = checkIfElevatorExistsOnSame(level, freeElevators);
//        freeElevators
//                .toStream()
//                .anyMatch(elevator -> Objects.equals(elevator.getCurrentLevel(), level));
//
//        if (elevatorExistOnSameLevel) {
//            return findOneElevatorAtSpecific(level);
//        } else {
//        val ref = new Object() {
//            String closestElevatorId = null;
//            int minDifference = Integer.MAX_VALUE;
//        };
//        return freeElevators.flatMap(freeElevator -> {
//                    int levelDifference = Math.abs(freeElevator.getCurrentLevel() - level);
//
//                    if (Math.min(ref.minDifference, levelDifference) != ref.minDifference) {
//                        ref.closestElevatorId = freeElevator.getId();
//                        ref.minDifference = levelDifference;
//                    }
//                    return elevatorRepository.findById(ref.closestElevatorId);
//                });
    }

//    private Mono<Elevator> findFreeElevator() {
//        return elevatorRepository
//                .findAll()
//                .filter(elevator -> elevator.getState() == ElevatorState.IDLE);
//    }

    private boolean checkIfElevatorExistsOnSame(Integer level, Flux<Elevator> freeElevators) {
        return freeElevators
                .toStream()
                .anyMatch(elevator -> Objects.equals(elevator.getCurrentLevel(), level));
    }

    private Mono<Elevator> findOneElevatorAtSpecific(Integer level) {
        return elevatorRepository
                .findByCurrentLevel(level)
                .next()
                .onErrorComplete(NoSuchElementException.class);
    }
}
