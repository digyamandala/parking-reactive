package com.parking.reactive.web.controller;

import com.parking.reactive.command.parent.Command;
import reactor.core.publisher.Mono;

public interface Executor {

  <T, R> Mono<R> execute(Class<? extends Command<T, R>> commandClass, T commandRequest);
}
