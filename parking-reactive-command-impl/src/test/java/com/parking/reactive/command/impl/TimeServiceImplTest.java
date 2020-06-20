package com.parking.reactive.command.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TimeServiceImplTest {

  @InjectMocks
  private TimeServiceImpl timeServiceImpl;

  @Test
  void getCurrentTimeMillis() {

    assertThat(timeServiceImpl.getCurrentTimeMillis()).isNotNull();
  }
}
