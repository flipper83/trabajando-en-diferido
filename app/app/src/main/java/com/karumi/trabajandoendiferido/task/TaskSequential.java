package com.karumi.trabajandoendiferido.task;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.karumi.trabajandoendiferido.api.ApiCall;
import com.karumi.trabajandoendiferido.api.response.ApiResponse;
import com.karumi.trabajandoendiferido.task.job.BaseJob;
import com.karumi.trabajandoendiferido.ui.Ui;
import com.path.android.jobqueue.JobManager;
import java.io.IOException;
import retrofit.Response;

/**
 *
 */
public class TaskSequential implements Task {
  private final ApiCall apiCall;
  private final Context context;

  public TaskSequential(ApiCall apiCall, Context context) {
    this.apiCall = apiCall;
    this.context = context;
  }

  @Override public void executeTask(Ui ui, int totalTask) {
    JobManager jobManager = new JobManager(context);
    jobManager.addJobInBackground(new ApiCallJob(apiCall, ui, totalTask));
  }

  private class ApiCallJob extends BaseJob {

    Handler handler = new Handler();
    private static final String LOGTAG = "apiCallJob";
    private final int totalTasks;
    private final ApiCall apiCall;
    private final Ui ui;

    public ApiCallJob(ApiCall apiCall, Ui ui, int totalTasks) {
      super();
      this.apiCall = apiCall;
      this.totalTasks = totalTasks;
      this.ui = ui;
    }

    @Override protected void execute() {
      try {
        for (int i = 0; i < totalTasks; i++) {
          Response<ApiResponse> apiResponseResponse = apiCall.callSync(i + 1);
        }
        sendSuccess(System.currentTimeMillis());
      } catch (IOException e) {
        Log.d(LOGTAG, "error ", e);
        sendError("error " + e);
      }
    }

    private void sendError(final String error) {
      handler.post(new Runnable() {
        @Override public void run() {
          ui.showError(error);
        }
      });
    }

    private void sendSuccess(final long timeMillis) {
      handler.post(new Runnable() {
        @Override public void run() {
          ui.showTime(timeMillis);
        }
      });
    }
  }
}
