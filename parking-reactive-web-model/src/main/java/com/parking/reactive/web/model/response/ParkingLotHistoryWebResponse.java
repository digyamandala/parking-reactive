package com.parking.reactive.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParkingLotHistoryWebResponse {

  private Long duration;

  private Integer floor;

  private Long inTime;

  private Integer number;

  private Long outTime;

  private String plateNumber;

  private Long price;
}
