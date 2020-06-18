package com.parking.reactive.repository;

import com.parking.reactive.repository.model.ParkingLotHistory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ParkingLotHistoryRepository extends ReactiveMongoRepository<ParkingLotHistory, String> {

}
