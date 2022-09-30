package com.example.msselevator.repository;

import com.example.msselevator.domain.Elevator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElevatorRepository extends JpaRepository<Elevator, Integer> {
    List<Elevator> findByCurrentLevel(Integer level);
}
