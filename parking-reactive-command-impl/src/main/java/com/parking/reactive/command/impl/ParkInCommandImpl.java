package com.parking.reactive.command.impl;

import com.parking.reactive.command.ParkInCommand;
import com.parking.reactive.command.TimeHelper;
import com.parking.reactive.command.helper.WebResponseHelper;
import com.parking.reactive.command.model.request.ParkInCommandRequest;
import com.parking.reactive.repository.ParkingLotRepository;
import com.parking.reactive.repository.model.ParkingLot;
import com.parking.reactive.web.model.response.ParkingLotWebResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ParkInCommandImpl implements ParkInCommand {

  private final ParkingLotRepository parkingLotRepository;

  private final TimeHelper timeHelper;

  public ParkInCommandImpl(ParkingLotRepository parkingLotRepository,
      TimeHelper timeHelper) {
    this.parkingLotRepository = parkingLotRepository;
    this.timeHelper = timeHelper;
  }

  @Override
  public Mono<ParkingLotWebResponse> execute(ParkInCommandRequest commandRequest) {
    return Mono.defer(() -> parkingLotRepository.findFirstByOccupiedOrderByFloorAsc(false))
        .map(parkingLot -> setToOccupied(commandRequest, parkingLot))
        .flatMap(parkingLotRepository::save)
        .map(WebResponseHelper::toParkingLotWebResponse);
  }

  private ParkingLot setToOccupied(ParkInCommandRequest commandRequest, ParkingLot parkingLot) {
    parkingLot.setOccupied(Boolean.TRUE);
    parkingLot.setPlateNumber(commandRequest.getPlateNumber());
    parkingLot.setInTime(timeHelper.getCurrentTimeMillis());
    return parkingLot;
  }
}
