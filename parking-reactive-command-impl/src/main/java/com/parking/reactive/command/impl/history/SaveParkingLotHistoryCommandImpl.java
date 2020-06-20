package com.parking.reactive.command.impl.history;

import com.parking.reactive.command.SaveParkingLotHistoryCommand;
import com.parking.reactive.command.TimeService;
import com.parking.reactive.command.helper.PriceHelper;
import com.parking.reactive.command.helper.TimeHelper;
import com.parking.reactive.command.helper.WebResponseHelper;
import com.parking.reactive.command.model.request.SaveParkingLotHistoryCommandRequest;
import com.parking.reactive.repository.ParkingLotHistoryRepository;
import com.parking.reactive.repository.model.ParkingLotHistory;
import com.parking.reactive.web.model.response.ParkingLotHistoryWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SaveParkingLotHistoryCommandImpl implements SaveParkingLotHistoryCommand {

  private final ParkingLotHistoryRepository parkingLotHistoryRepository;

  private final TimeService timeService;

  @Autowired
  public SaveParkingLotHistoryCommandImpl(
      ParkingLotHistoryRepository parkingLotHistoryRepository, TimeService timeService) {
    this.parkingLotHistoryRepository = parkingLotHistoryRepository;
    this.timeService = timeService;
  }

  @Override
  public Mono<ParkingLotHistoryWebResponse> execute(SaveParkingLotHistoryCommandRequest commandRequest) {
    return Mono.fromSupplier(() -> createParkingLotHistory(commandRequest))
        .flatMap(parkingLotHistoryRepository::save)
        .map(WebResponseHelper::toParkingLotHistoryWebResponse);
  }

  private ParkingLotHistory createParkingLotHistory(SaveParkingLotHistoryCommandRequest commandRequest) {
    long nowMillis = timeService.getCurrentTimeMillis();
    Long duration = TimeHelper.getDuration(commandRequest.getInTime(), nowMillis);
    return ParkingLotHistory.builder()
        .plateNumber(commandRequest.getPlateNumber())
        .floor(commandRequest.getFloor())
        .number(commandRequest.getNumber())
        .inTime(commandRequest.getInTime())
        .outTime(nowMillis)
        .duration(duration)
        .price(PriceHelper.getPrice(duration))
        .build();
  }
}
