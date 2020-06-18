package com.parking.reactive.command;

import com.parking.reactive.command.model.request.GetParkingLotsCommandRequest;
import com.parking.reactive.command.parent.Command;
import com.parking.reactive.web.model.response.ParkingLotWebResponse;

import java.util.List;

public interface GetParkingLotsCommand extends Command<GetParkingLotsCommandRequest, List<ParkingLotWebResponse>> {

}
