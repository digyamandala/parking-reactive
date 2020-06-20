package com.parking.reactive.command.impl;

import com.parking.reactive.command.TimeService;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TimeServiceImpl implements TimeService {

  @Override
  public Long getCurrentTimeMillis() {
    return Instant.now()
        .toEpochMilli();
  }
}
