package com.parking.reactive.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = CollectionNames.PARKING_LOT_HISTORY)
public class ParkingLotHistory {

  private Long duration;

  private Integer floor;

  @Id
  private String id;

  private Long inTime;

  private Integer number;

  private Long outTime;

  private String plateNumber;
  
  private Long price;
}
