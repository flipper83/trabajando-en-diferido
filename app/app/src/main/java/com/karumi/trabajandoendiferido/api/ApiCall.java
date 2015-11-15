package com.karumi.trabajandoendiferido.api;

import com.karumi.trabajandoendiferido.api.response.ApiResponse;
import com.karumi.trabajandoendiferido.server.ApiRest;
import com.karumi.trabajandoendiferido.server.MockApiCalls;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 *
 */
public class ApiCall {
  private final MockApiCalls mockApiCalls;
  private ApiRest apiRest;

  public ApiCall(MockApiCalls mockApiCalls) {
    this.mockApiCalls = mockApiCalls;
  }

  public void init() {
    OkHttpClient client = new OkHttpClient();
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    client.interceptors().add(interceptor);

    Retrofit retrofit = new Retrofit.Builder().baseUrl(mockApiCalls.getUrl())
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    apiRest = retrofit.create(ApiRest.class);
  }

  public Call<ApiResponse> callOneAsync() {
    return apiRest.apiOne();
  }
}
