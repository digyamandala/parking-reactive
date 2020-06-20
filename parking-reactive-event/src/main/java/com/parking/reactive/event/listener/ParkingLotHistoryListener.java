package com.parking.reactive.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.reactive.command.SaveParkingLotHistoryCommand;
import com.parking.reactive.command.model.event.Topics;
import com.parking.reactive.command.model.request.SaveParkingLotHistoryCommandRequest;
import com.parking.reactive.repository.model.ParkingLotHistory;
import com.parking.reactive.web.controller.Executor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

@Component
@Slf4j
public class ParkingLotHistoryListener {

  private final ObjectMapper objectMapper;

  private final Executor executor;

  @Autowired
  public ParkingLotHistoryListener(ObjectMapper objectMapper, Executor executor) {
    this.objectMapper = objectMapper;
    this.executor = executor;
  }

  @KafkaListener(topics = Topics.SAVE_PARKING_LOT_HISTORY)
  public void save(String message) throws JsonProcessingException {
    ParkingLotHistory parkingLotHistory = objectMapper.readValue(message, ParkingLotHistory.class);

    executor.execute(SaveParkingLotHistoryCommand.class, toSaveParkingLotHistoryCommandRequest(parkingLotHistory))
        .subscribeOn(Schedulers.elastic())
        .doOnSubscribe(subscription -> log.info("save parking lot history is called"))
        .doOnSuccess(response -> log.info("Save history for {}", parkingLotHistory.getPlateNumber()))
        .doOnError(throwable -> log.error("Error saving {}, exception: {}", parkingLotHistory.getPlateNumber(),
            throwable.getMessage()))
        .subscribe();
  }

  private SaveParkingLotHistoryCommandRequest toSaveParkingLotHistoryCommandRequest(
      ParkingLotHistory parkingLotHistory) {
    return SaveParkingLotHistoryCommandRequest.builder()
        .floor(parkingLotHistory.getFloor())
        .inTime(parkingLotHistory.getInTime())
        .number(parkingLotHistory.getNumber())
        .plateNumber(parkingLotHistory.getPlateNumber())
        .build();
  }
}
