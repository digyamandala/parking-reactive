package com.parking.reactive.command.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveParkingLotHistoryCommandRequest {

  private Integer floor;

  private Long inTime;

  private Integer number;

  private String plateNumber;
}
