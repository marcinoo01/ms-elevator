package com.example.msselevator.service;

import com.example.msselevator.domain.Elevator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ElevatorService {
    Flux<Elevator> status();

    Mono<Elevator> call(Integer level);

    Mono<Elevator> request(String id, Integer requestLevel);

    Flux<Elevator> pickOne();
}
