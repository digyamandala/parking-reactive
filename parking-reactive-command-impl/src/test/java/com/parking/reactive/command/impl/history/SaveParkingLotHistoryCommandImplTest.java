package com.parking.reactive.command.impl.history;

import com.parking.reactive.command.TimeService;
import com.parking.reactive.command.model.request.SaveParkingLotHistoryCommandRequest;
import com.parking.reactive.repository.ParkingLotHistoryRepository;
import com.parking.reactive.repository.model.ParkingLotHistory;
import com.parking.reactive.web.model.response.ParkingLotHistoryWebResponse;
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
class SaveParkingLotHistoryCommandImplTest {

  private static final String PLATE_NUMBER = "plate-number";

  @InjectMocks
  private SaveParkingLotHistoryCommandImpl command;

  @Mock
  private ParkingLotHistoryRepository parkingLotHistoryRepository;

  @Mock
  private TimeService timeService;

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(parkingLotHistoryRepository, timeService);
  }

  @Test
  void execute() {
    SaveParkingLotHistoryCommandRequest commandRequest = SaveParkingLotHistoryCommandRequest.builder()
        .plateNumber(PLATE_NUMBER)
        .inTime(1L)
        .build();

    when(timeService.getCurrentTimeMillis()).thenReturn(2L);

    ParkingLotHistory parkingLotHistory = ParkingLotHistory.builder()
        .plateNumber(PLATE_NUMBER)
        .price(2000L)
        .outTime(2L)
        .inTime(1L)
        .duration(1L)
        .build();

    when(parkingLotHistoryRepository.save(parkingLotHistory)).thenReturn(Mono.just(parkingLotHistory));

    ParkingLotHistoryWebResponse expected = ParkingLotHistoryWebResponse.builder()
        .plateNumber(PLATE_NUMBER)
        .outTime(2L)
        .inTime(1L)
        .duration(1L)
        .price(2000L)
        .build();

    StepVerifier.create(command.execute(commandRequest))
        .expectNext(expected)
        .verifyComplete();
  }
}
