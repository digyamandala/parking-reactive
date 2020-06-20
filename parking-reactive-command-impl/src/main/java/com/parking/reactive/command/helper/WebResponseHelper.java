package com.parking.reactive.command.helper;

import com.parking.reactive.repository.model.ParkingLot;
import com.parking.reactive.repository.model.ParkingLotHistory;
import com.parking.reactive.web.model.response.ParkingLotHistoryWebResponse;
import com.parking.reactive.web.model.response.ParkingLotWebResponse;

public final class WebResponseHelper {

  public static ParkingLotWebResponse toParkingLotWebResponse(ParkingLot parkingLot) {
    return ParkingLotWebResponse.builder()
        .floor(parkingLot.getFloor())
        .inTime(parkingLot.getInTime())
        .number(parkingLot.getNumber())
        .occupied(parkingLot.isOccupied())
        .plateNumber(parkingLot.getPlateNumber())
        .build();
  }

  public static ParkingLotHistoryWebResponse toParkingLotHistoryWebResponse(ParkingLotHistory parkingLotHistory) {
    return ParkingLotHistoryWebResponse.builder()
        .floor(parkingLotHistory.getFloor())
        .inTime(parkingLotHistory.getInTime())
        .number(parkingLotHistory.getNumber())
        .outTime(parkingLotHistory.getOutTime())
        .plateNumber(parkingLotHistory.getPlateNumber())
        .price(parkingLotHistory.getPrice())
        .duration(parkingLotHistory.getDuration())
        .build();
  }
}
