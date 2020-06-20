package com.parking.reactive.command.impl;

import com.parking.reactive.command.ParkOutCommand;
import com.parking.reactive.command.TimeService;
import com.parking.reactive.command.event.config.KafkaProducer;
import com.parking.reactive.command.helper.PriceHelper;
import com.parking.reactive.command.helper.TimeHelper;
import com.parking.reactive.command.helper.WebResponseHelper;
import com.parking.reactive.command.model.event.Topics;
import com.parking.reactive.command.model.request.ParkOutCommandRequest;
import com.parking.reactive.repository.ParkingLotRepository;
import com.parking.reactive.repository.model.ParkingLot;
import com.parking.reactive.web.model.response.ParkingLotWebResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ParkOutCommandImpl implements ParkOutCommand {

  private final ParkingLotRepository parkingLotRepository;

  private final KafkaProducer kafkaProducer;

  private final TimeService timeService;

  public ParkOutCommandImpl(ParkingLotRepository parkingLotRepository,
      KafkaProducer kafkaProducer, TimeService timeService) {
    this.parkingLotRepository = parkingLotRepository;
    this.kafkaProducer = kafkaProducer;
    this.timeService = timeService;
  }

  @Override
  public Mono<ParkingLotWebResponse> execute(ParkOutCommandRequest parkOutCommandRequest) {
    return Mono.defer(() -> findParkingLot(parkOutCommandRequest))
        .map(this::unsetParkingLot)
        .doOnNext(this::sendEvent)
        .map(WebResponseHelper::toParkingLotWebResponse)
        .doOnNext(this::setPriceAndOutTime);
  }

  private void setPriceAndOutTime(ParkingLotWebResponse parkingLotWebResponse) {
    long now = timeService.getCurrentTimeMillis();
    long duration = TimeHelper.getDuration(parkingLotWebResponse.getInTime(), now);
    parkingLotWebResponse.setPrice(PriceHelper.getPrice(duration));
    parkingLotWebResponse.setDuration(duration);
    parkingLotWebResponse.setOutTime(now);
    parkingLotWebResponse.setOccupied(false);
  }

  private void sendEvent(ParkingLot parkingLot) {
    kafkaProducer.send(Topics.SAVE_PARKING_LOT_HISTORY, parkingLot.getId(), parkingLot);
  }

  private ParkingLot unsetParkingLot(ParkingLot parkingLot) {
    ParkingLot beforeUpdated = new ParkingLot();
    BeanUtils.copyProperties(parkingLot, beforeUpdated);

    parkingLot.setPlateNumber(null);
    parkingLot.setInTime(null);
    parkingLot.setOccupied(Boolean.FALSE);

    parkingLotRepository.save(parkingLot)
        .subscribe();
    return beforeUpdated;
  }

  private Mono<ParkingLot> findParkingLot(ParkOutCommandRequest parkOutCommandRequest) {
    return parkingLotRepository.findByFloorAndNumberAndPlateNumber(parkOutCommandRequest.getFloor(),
        parkOutCommandRequest.getNumber(), parkOutCommandRequest.getPlateNumber());
  }
}
