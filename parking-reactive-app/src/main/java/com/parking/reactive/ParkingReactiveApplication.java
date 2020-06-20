package com.parking.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackages = {"com.parking.reactive.repository"})
public class ParkingReactiveApplication {

  public static void main(String[] args) {
    SpringApplication.run(ParkingReactiveApplication.class, args);
  }
}
