package com.parking.reactive.command.impl;

import com.parking.reactive.command.GetParkingLotsCommand;
import com.parking.reactive.command.TimeHelper;
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
import java.util.concurrent.TimeUnit;

@Service
public class GetParkingLotsCommandImpl implements GetParkingLotsCommand {

  private final ParkingLotRepository parkingLotRepository;

  private final TimeHelper timeHelper;

  public GetParkingLotsCommandImpl(ParkingLotRepository parkingLotRepository,
      TimeHelper timeHelper) {
    this.parkingLotRepository = parkingLotRepository;
    this.timeHelper = timeHelper;
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
    Long currentTime = timeHelper.getCurrentTimeMillis();
    Long duration = getDuration(parkingLotWebResponse.getInTime(), currentTime);
    parkingLotWebResponse.setDuration(duration);
    Optional.ofNullable(duration)
        .ifPresent(duration1 -> parkingLotWebResponse.setPrice(getPrice(duration1)));
  }

  private Long getPrice(Long duration) {
    if (duration <= 0) {
      return 2000L;
    } else if (duration > 10) {
      return 20000L;
    } else {
      return duration * 2000;
    }
  }

  private static Long getDuration(Long inDate, long current) {
    return Optional.ofNullable(inDate)
        .map(in -> TimeUnit.MILLISECONDS.toHours(current - inDate))
        .orElse(null);
  }
}
