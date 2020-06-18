package com.parking.reactive.repository;

import com.parking.reactive.repository.model.ParkingLot;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ParkingLotRepository extends ReactiveMongoRepository<ParkingLot, String>, ParkingLotRepositoryCustom {

  Mono<ParkingLot> findFirstByOccupiedOrderByFloorAsc(Boolean occupied);

  Mono<ParkingLot> findByFloorAndNumberAndPlateNumber(Integer floor, Integer number, String plateNumber);
}
