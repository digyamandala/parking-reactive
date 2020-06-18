package com.parking.reactive.command.impl;

import com.parking.reactive.command.InitializeParkingLotCommand;
import com.parking.reactive.command.model.request.InitializeParkingLotCommandRequest;
import com.parking.reactive.repository.ParkingLotRepository;
import com.parking.reactive.repository.model.ParkingLot;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class InitializeParkingLotCommandImpl implements InitializeParkingLotCommand {

  private final ParkingLotRepository parkingLotRepository;

  public InitializeParkingLotCommandImpl(ParkingLotRepository parkingLotRepository) {
    this.parkingLotRepository = parkingLotRepository;
  }

  @Override
  public Mono<Boolean> execute(InitializeParkingLotCommandRequest commandRequest) {
    return Mono.fromSupplier(() -> commandRequest)
        .doOnNext(commandRequest1 -> parkingLotRepository.deleteAll()
            .subscribe())
        .map(commandRequest1 -> collectParkingLot())
        .flatMap(parkingLots -> parkingLotRepository.saveAll(parkingLots)
            .ignoreElements())
        .map(parkingLot -> Boolean.TRUE);
  }

  private List<ParkingLot> collectParkingLot() {
    return IntStream.range(1, 6)
        .mapToObj(floor -> IntStream.range(1, 21)
            .mapToObj(number -> toParkingLot(floor, number))
            .collect(Collectors.toList()))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  private ParkingLot toParkingLot(int floor, int number) {
    return ParkingLot.builder()
        .floor(floor)
        .number(number)
        .build();
  }
}


