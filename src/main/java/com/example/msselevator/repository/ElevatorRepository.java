package com.example.msselevator.repository;

import com.example.msselevator.domain.Elevator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElevatorRepository extends JpaRepository<Elevator, Integer> {
    Elevator findByLevel(Integer level);
}
