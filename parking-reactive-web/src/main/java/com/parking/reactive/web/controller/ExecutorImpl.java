package com.parking.reactive.web.controller;

import com.parking.reactive.command.parent.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ExecutorImpl implements Executor {

  private final ApplicationContext applicationContext;

  @Autowired
  public ExecutorImpl(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public <T, R> Mono<R> execute(Class<? extends Command<T, R>> commandClass, T commandRequest) {
    Command<T, R> command = applicationContext.getBean(commandClass);
    return command.execute(commandRequest);
  }
}
