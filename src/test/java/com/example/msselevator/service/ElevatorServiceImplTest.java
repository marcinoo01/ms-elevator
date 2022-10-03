package com.example.msselevator.service;

import com.example.msselevator.repository.ElevatorRepository;
import com.example.msselevator.util.MockUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ElevatorServiceImplTest {

    @Mock
    private ElevatorRepository elevatorRepository;

    @InjectMocks
    private ElevatorServiceImpl elevatorService;

    @Test
    void checkIfNoSuchElementIsThrownWhenNoneOfObjectsExists() {
        Exception exception = assertThrows(NoSuchElementException.class, () -> elevatorService.call(1));
        assertEquals("Requested elevator with id: 0 does not exist", exception.getMessage());
    }

    @Test
    void checkIfAfterRequestStateIsChanged() {
        given(elevatorRepository.findAll()).willReturn(List.of(MockUtil.IDLE_ELEVATOR_LEVEL_0, MockUtil.IDLE_ELEVATOR_LEVEL_5, MockUtil.IDLE_ELEVATOR_LEVEL_3));
        given(elevatorRepository.findById(0)).willReturn(Optional.of(MockUtil.IDLE_ELEVATOR_LEVEL_0));
        given(elevatorRepository.getReferenceById(MockUtil.IDLE_ELEVATOR_LEVEL_0.getId())).willReturn(MockUtil.IDLE_ELEVATOR_LEVEL_0);
        elevatorService.call(1);
        verify(MockUtil.IDLE_ELEVATOR_LEVEL_0).setCurrentLevel(1);
    }
}
