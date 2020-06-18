package com.parking.reactive.command.impl;

import com.parking.reactive.command.ParkOutCommand;
import com.parking.reactive.command.model.request.ParkOutCommandRequest;
import com.parking.reactive.repository.ParkingLotHistoryRepository;
import com.parking.reactive.repository.ParkingLotRepository;
import com.parking.reactive.repository.model.ParkingLot;
import com.parking.reactive.repository.model.ParkingLotHistory;
import com.parking.reactive.web.model.response.ParkingLotWebResponse;
import helper.WebResponseHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ParkOutCommandImpl implements ParkOutCommand {

  private final ParkingLotRepository parkingLotRepository;

  private final ParkingLotHistoryRepository parkingLotHistoryRepository;

  public ParkOutCommandImpl(ParkingLotRepository parkingLotRepository,
      ParkingLotHistoryRepository parkingLotHistoryRepository) {
    this.parkingLotRepository = parkingLotRepository;
    this.parkingLotHistoryRepository = parkingLotHistoryRepository;
  }

  @Override
  public Mono<ParkingLotWebResponse> execute(ParkOutCommandRequest parkOutCommandRequest) {
    return Mono.defer(() -> findParkingLot(parkOutCommandRequest))
        .doOnNext(this::saveParkingLotHistory)
        .map(this::unOccupyParkingLot)
        .map(WebResponseHelper::toParkingLotWebResponse);
  }

  private void saveParkingLotHistory(ParkingLot parkingLot) {
    Mono.fromSupplier(() -> toParkingLotHistory(parkingLot))
        .doOnNext(parkingLotHistory -> parkingLotHistoryRepository.save(parkingLotHistory)
            .subscribe());
  }

  private ParkingLotHistory toParkingLotHistory(ParkingLot parkingLot) {
    return ParkingLotHistory.builder()
        .floor(parkingLot.getFloor())
        .inTime(parkingLot.getInTime())
        .number(parkingLot.getNumber())
        .outTime(parkingLot.getOutTime())
        .plateNumber(parkingLot.getPlateNumber())

        .build();
  }

  private ParkingLot unOccupyParkingLot(ParkingLot parkingLot) {
    ParkingLot beforeUpdated = new ParkingLot();
    BeanUtils.copyProperties(parkingLot, beforeUpdated);

    parkingLot.setPlateNumber(null);
    parkingLot.setOccupied(Boolean.FALSE);
    parkingLot.setInTime(null);
    parkingLot.setOutTime(null);

    parkingLotRepository.save(parkingLot)
        .subscribe();
    return beforeUpdated;
  }

  private Mono<ParkingLot> findParkingLot(ParkOutCommandRequest parkOutCommandRequest) {
    return parkingLotRepository.findByFloorAndNumberAndPlateNumber(parkOutCommandRequest.getFloor(),
        parkOutCommandRequest.getNumber(), parkOutCommandRequest.getPlateNumber());
  }
}
