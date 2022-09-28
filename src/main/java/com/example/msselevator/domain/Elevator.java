package com.example.msselevator.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "elevator")
public class Elevator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer level;
    @ElementCollection
    private Set<Integer> targetLevels;
    @Enumerated(EnumType.STRING)
    private ElevatorState state;
}