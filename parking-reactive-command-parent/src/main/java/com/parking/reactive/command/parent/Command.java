package com.parking.reactive.command.parent;

import reactor.core.publisher.Mono;

public interface Command<T, R> {

  Mono<R> execute(T t);
}
