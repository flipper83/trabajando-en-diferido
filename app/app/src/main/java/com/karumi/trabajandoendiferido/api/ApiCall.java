package com.karumi.trabajandoendiferido.api;

import com.karumi.trabajandoendiferido.api.response.ApiResponse;
import com.karumi.trabajandoendiferido.server.ApiRest;
import com.karumi.trabajandoendiferido.server.MockApiCalls;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import java.io.IOException;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
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

  public Response<ApiResponse> callSync(int type) throws IOException {
    Call<ApiResponse> call = apiRest.api(type);
    return call.execute();
  }

  public void callAsync(int type, Callback<ApiResponse> callback) {
    Call<ApiResponse> call = apiRest.api(type);
    call.enqueue(callback);
  }
}
