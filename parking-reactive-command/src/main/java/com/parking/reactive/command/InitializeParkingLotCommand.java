package com.parking.reactive.command;

import com.parking.reactive.command.model.request.InitializeParkingLotCommandRequest;
import com.parking.reactive.command.parent.Command;

public interface InitializeParkingLotCommand extends Command<InitializeParkingLotCommandRequest, Boolean> {

}
