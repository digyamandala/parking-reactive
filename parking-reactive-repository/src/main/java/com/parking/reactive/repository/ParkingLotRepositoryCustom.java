package com.parking.reactive.repository;

import com.parking.reactive.repository.model.ParkingLot;
import reactor.core.publisher.Flux;

public interface ParkingLotRepositoryCustom {

  Flux<ParkingLot> findAllDynamic(Integer floor, Integer number, Long inTime, Long outTime, Boolean occupied,
      String plateNumber);
}
