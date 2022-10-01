package com.example.msselevator.repository;

import com.example.msselevator.domain.Elevator;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ElevatorRepository extends ReactiveMongoRepository<Elevator, String> {
    Flux<Elevator> findByCurrentLevel(Integer level);
}
