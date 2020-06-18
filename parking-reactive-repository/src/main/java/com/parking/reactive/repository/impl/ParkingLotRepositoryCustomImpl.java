package com.parking.reactive.repository.impl;

import com.parking.reactive.repository.ParkingLotRepositoryCustom;
import com.parking.reactive.repository.model.ParkingLot;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.Optional;

@Repository
public class ParkingLotRepositoryCustomImpl implements ParkingLotRepositoryCustom {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  public ParkingLotRepositoryCustomImpl(
      ReactiveMongoTemplate reactiveMongoTemplate) {
    this.reactiveMongoTemplate = reactiveMongoTemplate;
  }

  @Override
  public Flux<ParkingLot> findAllDynamic(Integer floor, Integer number, Long inTime, Long outTime, Boolean occupied,
      String plateNumber) {
    if (Objects.isNull(floor) && Objects.isNull(number) && Objects.isNull(inTime) && Objects.isNull(
        outTime) && Objects.isNull(occupied) && Objects.isNull(plateNumber)) {
      return reactiveMongoTemplate.findAll(ParkingLot.class);
    } else {
      Criteria criteria = new Criteria();

      Optional.ofNullable(floor)
          .ifPresent(integer -> criteria.and("floor")
              .is(floor));

      Optional.ofNullable(number)
          .ifPresent(integer -> criteria.and("number")
              .is(number));

      Optional.ofNullable(inTime)
          .ifPresent(aLong -> criteria.and("inTime")
              .is(inTime));

      Optional.ofNullable(outTime)
          .ifPresent(aLong -> criteria.and("outTime")
              .is(outTime));

      Optional.ofNullable(plateNumber)
          .ifPresent(plate -> criteria.and("plateNumber")
              .is(plateNumber));

      Optional.ofNullable(occupied)
          .ifPresent(plate -> criteria.and("occupied")
              .is(occupied));

      Query query = Query.query(criteria);
      return reactiveMongoTemplate.find(query, ParkingLot.class);
    }
  }
}
