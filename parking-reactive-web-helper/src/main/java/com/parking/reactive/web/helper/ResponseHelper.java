package com.parking.reactive.web.helper;

import com.parking.reactive.web.model.response.response.Response;

public final class ResponseHelper {

  private ResponseHelper() {

  }

  public static <T> Response<T> ok(T data) {
    return Response.<T>builder()
        .status("OK")
        .code(200)
        .data(data)
        .build();
  }
}
