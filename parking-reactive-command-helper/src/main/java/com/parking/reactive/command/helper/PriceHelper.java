package com.parking.reactive.command.helper;

import java.util.Optional;

public class PriceHelper {

  public static Long getPrice(Long duration) {
    return Optional.ofNullable(duration)
        .map(PriceHelper::calculatePrice)
        .orElse(null);
  }

  private static Long calculatePrice(Long duration) {
    if (duration <= 0) {
      return 2000L;
    } else if (duration > 10) {
      return 20000L;
    } else {
      return duration * 2000;
    }
  }
}
