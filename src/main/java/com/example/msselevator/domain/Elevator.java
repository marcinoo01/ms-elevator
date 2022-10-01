package com.example.msselevator.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Document
@Builder
@AllArgsConstructor
public class Elevator {
    @Id
    private String id;
    private Integer currentLevel;
    private Integer targetLevel;
    private ElevatorState state;
}