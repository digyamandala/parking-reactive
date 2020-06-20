package com.parking.reactive.command.impl;

import com.parking.reactive.command.GetParkingLotsCommand;
import com.parking.reactive.command.TimeService;
import com.parking.reactive.command.helper.PriceHelper;
import com.parking.reactive.command.helper.TimeHelper;
import com.parking.reactive.command.helper.WebResponseHelper;
import com.parking.reactive.command.model.request.GetParkingLotsCommandRequest;
import com.parking.reactive.repository.ParkingLotRepository;
import com.parking.reactive.repository.model.ParkingLot;
import com.parking.reactive.web.model.response.ParkingLotWebResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class GetParkingLotsCommandImpl implements GetParkingLotsCommand {

  private final ParkingLotRepository parkingLotRepository;

  private final TimeService timeService;

  public GetParkingLotsCommandImpl(ParkingLotRepository parkingLotRepository,
      TimeService timeService) {
    this.parkingLotRepository = parkingLotRepository;
    this.timeService = timeService;
  }

  @Override
  public Mono<List<ParkingLotWebResponse>> execute(GetParkingLotsCommandRequest commandRequest) {
    return Flux.defer(() -> findParkingLots(commandRequest))
        .map(WebResponseHelper::toParkingLotWebResponse)
        .doOnNext(this::setDurationAndPrice)
        .collectList();
  }

  private Flux<ParkingLot> findParkingLots(GetParkingLotsCommandRequest commandRequest) {
    return parkingLotRepository.findAllDynamic(commandRequest.getFloor(), commandRequest.getNumber(),
        commandRequest.getInTime(), commandRequest.getOutTime(), commandRequest.getOccupied(),
        commandRequest.getPlateNumber());
  }

  private void setDurationAndPrice(ParkingLotWebResponse parkingLotWebResponse) {
    Long currentTime = timeService.getCurrentTimeMillis();
    Long duration = TimeHelper.getDuration(parkingLotWebResponse.getInTime(), currentTime);
    parkingLotWebResponse.setDuration(duration);
    Optional.ofNullable(duration)
        .ifPresent(duration1 -> parkingLotWebResponse.setPrice(PriceHelper.getPrice(duration1)));
  }
}
