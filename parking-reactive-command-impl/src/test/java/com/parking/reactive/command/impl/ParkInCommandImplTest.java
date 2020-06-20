package com.parking.reactive.command.impl;

import com.parking.reactive.command.TimeService;
import com.parking.reactive.command.model.request.ParkInCommandRequest;
import com.parking.reactive.repository.ParkingLotRepository;
import com.parking.reactive.repository.model.ParkingLot;
import com.parking.reactive.web.model.response.ParkingLotWebResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkInCommandImplTest {

  private static final String PLATE_NUMBER = "plate-number";

  private static final long IN_TIME = 10L;

  @InjectMocks
  private ParkInCommandImpl parkInCommand;

  @Mock
  private ParkingLotRepository parkingLotRepository;

  @Mock
  private TimeService timeService;

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(parkingLotRepository, timeService);
  }

  @Test
  void execute() {
    ParkInCommandRequest commandRequest = ParkInCommandRequest.builder()
        .plateNumber(PLATE_NUMBER)
        .build();

    ParkingLot parkingLot = ParkingLot.builder()
        .number(1)
        .floor(1)
        .build();

    when(parkingLotRepository.findFirstByOccupiedOrderByFloorAsc(false)).thenReturn(Mono.just(parkingLot));

    when(timeService.getCurrentTimeMillis()).thenReturn(IN_TIME);

    ParkingLot parkingLotAfterSave = ParkingLot.builder()
        .floor(1)
        .number(1)
        .occupied(true)
        .inTime(IN_TIME)
        .plateNumber(PLATE_NUMBER)
        .build();

    when(parkingLotRepository.save(parkingLotAfterSave)).thenReturn(Mono.just(parkingLotAfterSave));

    ParkingLotWebResponse expected = ParkingLotWebResponse.builder()
        .floor(1)
        .number(1)
        .occupied(true)
        .inTime(IN_TIME)
        .plateNumber(PLATE_NUMBER)
        .build();

    StepVerifier.create(parkInCommand.execute(commandRequest))
        .expectNext(expected)
        .verifyComplete();
  }
}
