package com.parking.reactive.command;

import com.parking.reactive.command.model.request.SaveParkingLotHistoryCommandRequest;
import com.parking.reactive.command.parent.Command;
import com.parking.reactive.web.model.response.ParkingLotHistoryWebResponse;

public interface SaveParkingLotHistoryCommand extends
    Command<SaveParkingLotHistoryCommandRequest, ParkingLotHistoryWebResponse> {

}
