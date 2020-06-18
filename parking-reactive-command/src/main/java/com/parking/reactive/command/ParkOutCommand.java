package com.parking.reactive.command;

import com.parking.reactive.command.model.request.ParkOutCommandRequest;
import com.parking.reactive.command.parent.Command;
import com.parking.reactive.web.model.response.ParkingLotWebResponse;

public interface ParkOutCommand extends Command<ParkOutCommandRequest, ParkingLotWebResponse> {

}
