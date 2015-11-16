package com.karumi.trabajandoendiferido.task;

import android.os.AsyncTask;
import android.util.Log;
import com.karumi.trabajandoendiferido.api.ApiCall;
import com.karumi.trabajandoendiferido.api.response.ApiResponse;
import com.karumi.trabajandoendiferido.ui.Ui;
import java.io.IOException;
import retrofit.Response;

public class TaskWithAsyncTask implements Task {
  private final ApiCall apiCall;

  public TaskWithAsyncTask(ApiCall apiCall) {
    this.apiCall = apiCall;
  }

  @Override public void executeTask(final Ui ui) {
    AsyncTask<Void, Void, ApiResponse> task = new AsyncTask<Void, Void, ApiResponse>() {
      @Override protected ApiResponse doInBackground(Void... voids) {
        try {
          Response<ApiResponse> apiResponseResponse = apiCall.callSync(1);
          if (apiResponseResponse.code() == 200) {
            return apiResponseResponse.body();
          }
        } catch (IOException e) {
          Log.d("TaskWithAsyncTask", "error", e);
        }

        return null;
      }

      @Override protected void onPostExecute(ApiResponse apiResponse) {
        if (apiResponse != null) {
          ui.showTime(System.currentTimeMillis());
        } else {
          ui.showError("error request");
        }
      }
    };

    task.execute();
  }
}
