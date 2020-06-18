package com.parking.reactive.command;

import com.parking.reactive.command.model.request.ParkInCommandRequest;
import com.parking.reactive.command.parent.Command;
import com.parking.reactive.web.model.response.ParkingLotWebResponse;

public interface ParkInCommand extends Command<ParkInCommandRequest, ParkingLotWebResponse> {

}
