package com.parking.reactive.web.controller;

import com.parking.reactive.command.GetParkingLotsCommand;
import com.parking.reactive.command.InitializeParkingLotCommand;
import com.parking.reactive.command.ParkInCommand;
import com.parking.reactive.command.ParkOutCommand;
import com.parking.reactive.command.model.request.GetParkingLotsCommandRequest;
import com.parking.reactive.command.model.request.InitializeParkingLotCommandRequest;
import com.parking.reactive.command.model.request.ParkInCommandRequest;
import com.parking.reactive.command.model.request.ParkOutCommandRequest;
import com.parking.reactive.web.helper.ResponseHelper;
import com.parking.reactive.web.model.response.ParkingLotWebResponse;
import com.parking.reactive.web.model.response.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
@RequestMapping(path = "/backend/parking", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ParkingLotController {

  private final Executor executor;

  public ParkingLotController(Executor executor) {
    this.executor = executor;
  }

  @GetMapping
  public Mono<Response<List<ParkingLotWebResponse>>> getParkingLots(
      @RequestParam(required = false) Integer floor, @RequestParam(required = false) Integer number,
      @RequestParam(required = false) Long inTime, @RequestParam(required = false) Long outTime,
      @RequestParam(required = false) Boolean occupied, @RequestParam(required = false) String plateNumber) {
    GetParkingLotsCommandRequest commandRequest = GetParkingLotsCommandRequest.builder()
        .floor(floor)
        .inTime(inTime)
        .number(number)
        .occupied(occupied)
        .outTime(outTime)
        .plateNumber(plateNumber)
        .build();
    log.info("CALLED");
    return executor.execute(GetParkingLotsCommand.class, commandRequest)
        .map(ResponseHelper::ok)
        .subscribeOn(Schedulers.elastic())
        .doOnSubscribe(subscription -> log.info("GET PARKING LOT CALLED"))
        .doOnSuccess(response -> log.info("GET FINISH"))
        .doOnError(throwable -> log.error("Get call is error, {}", throwable.getMessage()));
  }

  @PostMapping(path = "/_park-in")
  public Mono<Response<ParkingLotWebResponse>> parkIn(@RequestParam String plateNumber) {
    ParkInCommandRequest commandRequest = ParkInCommandRequest.builder()
        .plateNumber(plateNumber)
        .build();

    return executor.execute(ParkInCommand.class, commandRequest)
        .map(ResponseHelper::ok)
        .subscribeOn(Schedulers.elastic())
        .doOnSubscribe(subscription -> log.info("{} PARK IN HAS BEEN CALLED", plateNumber))
        .doOnSuccess(response -> log.info("{} HAS ENTERED", plateNumber))
        .doOnError(throwable -> log.error("Cannot process {} to park-in, Exception: {}", plateNumber,
            throwable.getMessage()));
  }

  @DeleteMapping(path = "/_park-out")
  public Mono<Response<ParkingLotWebResponse>> parkIn(@RequestParam String plateNumber, @RequestParam Integer floor,
      @RequestParam Integer number) {
    ParkOutCommandRequest commandRequest = ParkOutCommandRequest.builder()
        .plateNumber(plateNumber)
        .floor(floor)
        .number(number)
        .build();

    return executor.execute(ParkOutCommand.class, commandRequest)
        .map(ResponseHelper::ok)
        .subscribeOn(Schedulers.elastic())
        .doOnSubscribe(subscription -> log.info("{} PARK OUT HAS BEEN CALLED", plateNumber))
        .doOnSuccess(response -> log.info("{} HAS LEFT", plateNumber))
        .doOnError(throwable -> log.error("Cannot process {} to park-out, Exception: {}", plateNumber,
            throwable.getMessage()));
  }

  @PostMapping
  public Mono<Response<Boolean>> init() {

    log.info("CALLED");

    return executor.execute(InitializeParkingLotCommand.class, new InitializeParkingLotCommandRequest())
        .map(ResponseHelper::ok)
        .subscribeOn(Schedulers.elastic())
        .doOnSubscribe(subscription -> log.info("INIT HAS BEEN CALLED"))
        .doOnSuccess(response -> log.info("INIT SUCCESS"))
        .doOnError(throwable -> log.error("INIT FAILED, {}", throwable.getMessage()));
  }
}
