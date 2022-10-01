package com.example.msselevator.controller;

import com.example.msselevator.domain.Elevator;
import com.example.msselevator.service.ElevatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/elevators")
@RequiredArgsConstructor
public class ElevatorController {

    private final ElevatorService elevatorService;

    @GetMapping
    public Flux<Elevator> getElevators() {
        return elevatorService.status();
    }

    @PatchMapping
    public Mono<Elevator> callForElevator(@RequestBody Integer level) {
        return elevatorService.call(level);
    }

    @PatchMapping("/{id}")
    public Mono<Elevator> pickDestinationLevel(@PathVariable String id, @RequestBody Integer requestLevel) {
        return elevatorService.request(id, requestLevel);
    }
}
