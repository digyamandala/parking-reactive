package com.parking.reactive.command.impl;

import com.parking.reactive.command.ParkOutCommand;
import com.parking.reactive.command.helper.WebResponseHelper;
import com.parking.reactive.command.model.event.Topics;
import com.parking.reactive.command.model.request.ParkOutCommandRequest;
import com.parking.reactive.repository.ParkingLotRepository;
import com.parking.reactive.repository.model.ParkingLot;
import com.parking.reactive.web.model.response.ParkingLotWebResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ParkOutCommandImpl implements ParkOutCommand {

  private final ParkingLotRepository parkingLotRepository;

  private final KafkaTemplate kafkaTemplate;

  public ParkOutCommandImpl(ParkingLotRepository parkingLotRepository,
      KafkaTemplate kafkaTemplateLocal) {
    this.parkingLotRepository = parkingLotRepository;
    this.kafkaTemplate = kafkaTemplateLocal;
  }

  @Override
  public Mono<ParkingLotWebResponse> execute(ParkOutCommandRequest parkOutCommandRequest) {
    return Mono.defer(() -> findParkingLot(parkOutCommandRequest))
        .doOnNext(this::sendEvent)
        .map(this::unOccupyParkingLot)
        .map(WebResponseHelper::toParkingLotWebResponse);
  }

  private void sendEvent(ParkingLot parkingLot) {
    kafkaTemplate.send(Topics.SAVE_PARKING_LOT_HISTORY, parkingLot.getId(), parkingLot);
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
