package com.parking.reactive.command.impl;

import com.parking.reactive.command.ParkInCommand;
import com.parking.reactive.command.TimeService;
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

  private final TimeService timeService;

  public ParkInCommandImpl(ParkingLotRepository parkingLotRepository,
      TimeService timeService) {
    this.parkingLotRepository = parkingLotRepository;
    this.timeService = timeService;
  }

  @Override
  public Mono<ParkingLotWebResponse> execute(ParkInCommandRequest commandRequest) {
    return Mono.defer(this::findUnoccupied)
        .map(parkingLot -> setToOccupied(commandRequest, parkingLot))
        .flatMap(parkingLotRepository::save)
        .map(WebResponseHelper::toParkingLotWebResponse);
  }

  private Mono<ParkingLot> findUnoccupied() {
    return parkingLotRepository.findFirstByOccupiedOrderByFloorAsc(false);
  }

  private ParkingLot setToOccupied(ParkInCommandRequest commandRequest, ParkingLot parkingLot) {
    parkingLot.setOccupied(Boolean.TRUE);
    parkingLot.setPlateNumber(commandRequest.getPlateNumber());
    parkingLot.setInTime(timeService.getCurrentTimeMillis());
    return parkingLot;
  }
}
