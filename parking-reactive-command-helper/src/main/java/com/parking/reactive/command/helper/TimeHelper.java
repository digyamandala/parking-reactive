package com.parking.reactive.command.helper;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class TimeHelper {

  private TimeHelper() {
  }

  public static Long getDuration(Long inDate, long current) {
    return Optional.ofNullable(inDate)
        .map(in -> TimeUnit.MILLISECONDS.toHours(current - inDate) + 1)
        .orElse(null);
  }
}
