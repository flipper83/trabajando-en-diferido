package com.karumi.trabajandoendiferido.task;

import android.os.AsyncTask;
import android.util.Log;
import com.karumi.trabajandoendiferido.api.ApiCall;
import com.karumi.trabajandoendiferido.api.response.ApiResponse;
import com.karumi.trabajandoendiferido.ui.Ui;
import java.io.IOException;
import retrofit.Response;

public class TaskWithAsyncTask implements Task {
  private static int finishedTasks = 0;
  private final ApiCall apiCall;

  public TaskWithAsyncTask(ApiCall apiCall) {
    this.apiCall = apiCall;
  }

  @Override public void executeTask(final Ui ui, int totalTask) {
    for (int i = 1; i < totalTask + 1; i++) {
      AsyncTask<Void, Void, ApiResponse> task = new MyAsyncTask(ui, i, totalTask + 1);
      // from api 4 to api 11 threadPull after an before, one thread.
      //task.execute();
      //since api 11
      task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
  }

  class MyAsyncTask extends AsyncTask<Void, Void, ApiResponse> {

    private final Ui ui;
    private final int current;
    private final int total;

    public MyAsyncTask(Ui ui, int current, int total) {
      this.ui = ui;
      this.current = current;
      this.total = total;
    }

    @Override protected ApiResponse doInBackground(Void... voids) {
      try {
        Response<ApiResponse> apiResponseResponse = apiCall.callSync(current);
        if (apiResponseResponse.code() == 200) {
          return apiResponseResponse.body();
        }
      } catch (IOException e) {
        Log.d("TaskWithAsyncTask", "error", e);
      }

      return null;
    }

    @Override protected void onPostExecute(ApiResponse apiResponse) {
      TaskWithAsyncTask.finishedTasks++;
      if (TaskWithAsyncTask.finishedTasks >= total - 1) {
        if (apiResponse != null) {
          ui.showTime(System.currentTimeMillis());
        } else {
          ui.showError("error request");
        }
      }
    }
  }
}
