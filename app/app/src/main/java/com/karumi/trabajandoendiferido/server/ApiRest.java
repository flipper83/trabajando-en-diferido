package com.karumi.trabajandoendiferido.server;

import com.karumi.trabajandoendiferido.api.response.ApiResponse;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface ApiRest {
  @GET("{type}/") Call<ApiResponse> api(@Path("type") int callType);
  @GET("{type}/") Observable<ApiResponse> apiObservable(@Path("type") int callType);
}
