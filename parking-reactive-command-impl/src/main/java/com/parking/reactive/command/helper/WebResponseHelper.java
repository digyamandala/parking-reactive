package com.parking.reactive.command.helper;

import com.parking.reactive.repository.model.ParkingLot;
import com.parking.reactive.web.model.response.ParkingLotWebResponse;

public final class WebResponseHelper {

  public static ParkingLotWebResponse toParkingLotWebResponse(ParkingLot parkingLot) {
    return ParkingLotWebResponse.builder()
        .floor(parkingLot.getFloor())
        .inTime(parkingLot.getInTime())
        .number(parkingLot.getNumber())
        .occupied(parkingLot.isOccupied())
        .outTime(parkingLot.getOutTime())
        .plateNumber(parkingLot.getPlateNumber())
        .build();
  }
}
