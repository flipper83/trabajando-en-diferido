package com.karumi.trabajandoendiferido.server;

import com.karumi.trabajandoendiferido.api.response.ApiResponse;
import retrofit.Call;
import retrofit.http.GET;

public interface ApiRest {
  @GET("1/") Call<ApiResponse> apiOne();
}
