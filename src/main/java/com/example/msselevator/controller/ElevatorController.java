package com.example.msselevator.controller;

import com.example.msselevator.domain.Elevator;
import com.example.msselevator.service.ElevatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/elevators")
@RequiredArgsConstructor
public class ElevatorController {

    private final ElevatorService elevatorService;

    @GetMapping
    public ResponseEntity<List<Elevator>> getElevators(){
        return ResponseEntity.ok(elevatorService.status());
    }
}
