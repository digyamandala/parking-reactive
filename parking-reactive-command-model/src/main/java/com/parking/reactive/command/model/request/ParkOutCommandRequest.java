package com.parking.reactive.command.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParkOutCommandRequest {

  private Integer floor;

  private Integer number;

  private String plateNumber;
}
