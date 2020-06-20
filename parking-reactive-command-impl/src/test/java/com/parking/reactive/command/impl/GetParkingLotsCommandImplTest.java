package com.parking.reactive.command.impl;

import com.parking.reactive.command.TimeService;
import com.parking.reactive.command.model.request.GetParkingLotsCommandRequest;
import com.parking.reactive.repository.ParkingLotRepository;
import com.parking.reactive.repository.model.ParkingLot;
import com.parking.reactive.web.model.response.ParkingLotWebResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetParkingLotsCommandImplTest {

  private static final String PLATE_NUMBER = "plate-number";

  private static final long IN_TIME = 1592658385000L;

  private static final long CURRENT_TIME = 1592665585000L;

  @InjectMocks
  private GetParkingLotsCommandImpl command;

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
    GetParkingLotsCommandRequest commandRequest = GetParkingLotsCommandRequest.builder()
        .plateNumber(PLATE_NUMBER)
        .build();

    ParkingLot parkingLot = ParkingLot.builder()
        .plateNumber(PLATE_NUMBER)
        .inTime(IN_TIME)
        .occupied(true)
        .build();

    when(parkingLotRepository.findAllDynamic(null, null, null,
        null, null, PLATE_NUMBER)).thenReturn(Flux.just(parkingLot));

    when(timeService.getCurrentTimeMillis()).thenReturn(CURRENT_TIME);

    ParkingLotWebResponse expected = ParkingLotWebResponse.builder()
        .duration(3L)
        .plateNumber(PLATE_NUMBER)
        .price(6000L)
        .inTime(IN_TIME)
        .occupied(true)
        .build();

    StepVerifier.create(command.execute(commandRequest))
        .expectNext(Collections.singletonList(expected))
        .verifyComplete();
  }

  @Test
  void execute_notFound() {
    GetParkingLotsCommandRequest commandRequest = GetParkingLotsCommandRequest.builder()
        .plateNumber(PLATE_NUMBER)
        .build();

    when(parkingLotRepository.findAllDynamic(null, null, null,
        null, null, PLATE_NUMBER)).thenReturn(Flux.empty());

    StepVerifier.create(command.execute(commandRequest))
        .expectNext(Collections.emptyList())
        .verifyComplete();
  }
}
