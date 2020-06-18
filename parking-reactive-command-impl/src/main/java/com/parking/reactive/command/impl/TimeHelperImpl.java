package com.parking.reactive.command.impl;

import com.parking.reactive.command.TimeHelper;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TimeHelperImpl implements TimeHelper {

  @Override
  public Long getCurrentTimeMillis() {
    return Instant.now()
        .toEpochMilli();
  }
}
