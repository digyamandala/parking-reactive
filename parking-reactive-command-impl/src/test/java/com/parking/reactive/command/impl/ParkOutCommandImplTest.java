package com.parking.reactive.command.impl;

import com.parking.reactive.command.TimeService;
import com.parking.reactive.command.event.config.KafkaProducer;
import com.parking.reactive.command.model.event.Topics;
import com.parking.reactive.command.model.request.ParkOutCommandRequest;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkOutCommandImplTest {

  private static final String PLATE_NUMBER = "plate-number";

  @Mock
  private KafkaProducer kafkaProducer;

  @InjectMocks
  private ParkOutCommandImpl parkOutCommandImpl;

  @Mock
  private ParkingLotRepository parkingLotRepository;

  @Mock
  private TimeService timeService;

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(kafkaProducer, parkingLotRepository);
  }

  @Test
  void execute() {
    ParkOutCommandRequest commandRequest = ParkOutCommandRequest.builder()
        .plateNumber(PLATE_NUMBER)
        .number(1)
        .floor(1)
        .build();

    ParkingLot parkingLot = ParkingLot.builder()
        .plateNumber(PLATE_NUMBER)
        .number(1)
        .floor(1)
        .inTime(10L)
        .occupied(true)
        .build();

    when(parkingLotRepository.findByFloorAndNumberAndPlateNumber(1, 1, PLATE_NUMBER))
        .thenReturn(Mono.just(parkingLot));

    ParkingLot parkingLotKafka = ParkingLot.builder()
        .plateNumber(PLATE_NUMBER)
        .number(1)
        .floor(1)
        .inTime(10L)
        .occupied(true)
        .build();

    doNothing().when(kafkaProducer)
        .send(Topics.SAVE_PARKING_LOT_HISTORY, null, parkingLotKafka);

    ParkingLot parkingLotAfterUnset = ParkingLot.builder()
        .number(1)
        .floor(1)
        .build();

    when(parkingLotRepository.save(parkingLotAfterUnset)).thenReturn(Mono.just(parkingLotAfterUnset));

    when(timeService.getCurrentTimeMillis()).thenReturn(20L);

    ParkingLotWebResponse expected = ParkingLotWebResponse.builder()
        .plateNumber(PLATE_NUMBER)
        .number(1)
        .floor(1)
        .inTime(10L)
        .outTime(20L)
        .occupied(false)
        .duration(1L)
        .price(2000L)
        .build();

    StepVerifier.create(parkOutCommandImpl.execute(commandRequest))
        .expectNext(expected)
        .verifyComplete();
  }
}
