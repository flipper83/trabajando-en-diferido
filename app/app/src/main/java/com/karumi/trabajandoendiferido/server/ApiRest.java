package com.karumi.trabajandoendiferido.server;

import com.karumi.trabajandoendiferido.api.response.ApiResponse;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public interface ApiRest {
  @GET("{type}/") Call<ApiResponse> api(@Path("type") int callType);
}
