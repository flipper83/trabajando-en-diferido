package com.karumi.trabajandoendiferido.task;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.karumi.trabajandoendiferido.api.ApiCall;
import com.karumi.trabajandoendiferido.api.response.ApiResponse;
import com.karumi.trabajandoendiferido.task.job.BaseJob;
import com.karumi.trabajandoendiferido.ui.Ui;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import java.io.IOException;
import retrofit.Response;

public class TaskWithPriorityJobQueue implements Task {
  private static int finishedTasks = 0;
  private final ApiCall apiCall;
  private final Context context;
  private final boolean usingConsumer;

  public TaskWithPriorityJobQueue(ApiCall apiCall, Context context, boolean usingConsumer) {
    this.apiCall = apiCall;
    this.context = context;
    this.usingConsumer = usingConsumer;
  }

  @Override public void executeTask(final Ui ui, int totalTask) {
    finishedTasks = 0;

    JobManager jobManager;
    if (usingConsumer) {
      Configuration config = new Configuration.Builder(context)
          .minConsumerCount(1)//always keep at least one consumer alive
          .maxConsumerCount(3)//up to 3 consumers at a time
          .loadFactor(1)//jobs per consumer
          .build();
      jobManager = new JobManager(context, config);
    } else {
      jobManager = new JobManager(context);
    }

    for (int i = 1; i < totalTask + 1; i++) {
      jobManager.addJobInBackground(new ApiCallJob(apiCall, i, ui, totalTask));
    }
  }

  private class ApiCallJob extends BaseJob {

    private final int task;
    Handler handler = new Handler();
    private static final String LOGTAG = "apiCallJob";
    private final int totalTasks;
    private final ApiCall apiCall;
    private final Ui ui;

    public ApiCallJob(ApiCall apiCall, int task, Ui ui, int totalTasks) {
      super();
      this.apiCall = apiCall;
      this.totalTasks = totalTasks;
      this.ui = ui;
      this.task = task;
    }

    @Override protected void execute() {
      try {
        Response<ApiResponse> apiResponseResponse = apiCall.callSync(task + 1);
        finishedTasks++;
        if (finishedTasks >= totalTasks) {
          sendSuccess(System.currentTimeMillis());
        }
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
