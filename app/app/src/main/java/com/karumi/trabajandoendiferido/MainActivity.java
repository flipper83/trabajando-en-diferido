package com.karumi.trabajandoendiferido;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.karumi.trabajandoendiferido.api.ApiCall;
import com.karumi.trabajandoendiferido.server.MockApiCalls;
import com.karumi.trabajandoendiferido.task.Task;
import com.karumi.trabajandoendiferido.task.TaskWithAsyncTask;
import com.karumi.trabajandoendiferido.ui.Ui;
import trabajandoendiferido.karumi.com.trabajandoendiferido.R;

public class MainActivity extends Activity implements Ui {

  private TextView numThreadView;
  private TextView threadsView;
  private MockApiCalls mockApiCalls;

  Handler handler = new Handler();
  private Button asyncTaskButton;
  private TextView timeView;
  private long timeSending;
  private ApiCall apiCall;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (mockApiCalls == null) {
      mockApiCalls = new MockApiCalls();
      mockApiCalls.init();
    }
    mapUi();
  }

  private void mapUi() {
    numThreadView = ((TextView) findViewById(R.id.tv_num_thread));
    threadsView = ((TextView) findViewById(R.id.tv_threads));
    timeView = ((TextView) findViewById(R.id.tv_time));

    asyncTaskButton = ((Button) findViewById(R.id.bt_asynctask));
    asyncTaskButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        launchAsyncTask();
      }
    });
  }

  private void launchAsyncTask() {

    if (apiCall == null) {
      apiCall = new ApiCall(mockApiCalls);
      apiCall.init();
    }

    Task task = new TaskWithAsyncTask(apiCall);

    timeSending = System.currentTimeMillis();
    setTimeLoading();
    task.executeTask(this);
  }

  @Override protected void onResume() {
    super.onResume();
    updateThreads();
    // in on resume only exist 3 threads, binder and main.
  }

  @Override public void showError(String errorCode) {

  }

  @Override public void showTime(long time) {
    long timeDiff = System.currentTimeMillis() - timeSending;
    String timeComposed = getString(R.string.time_title) + " " + timeDiff;
    timeView.setText(timeComposed);
  }

  private void setTimeLoading() {
    String time = getString(R.string.time_title) + " " + getString(R.string.loading);
    timeView.setText(time);
  }

  private void updateThreads() {
    int count = Thread.activeCount();
    numThreadView.setText(count + " threads");

    Thread[] threads = new Thread[count];
    Thread.enumerate(threads);
    String threadsText = "";
    for (Thread thread : threads) {
      if (thread != null) {
        threadsText += thread.getName() +" "+ thread.getState().name()+ "/\n";

      }
    }
    threadsView.setText(threadsText);

    handler.postDelayed(refreshThread, 200);
  }

  private Runnable refreshThread = new Runnable() {
    @Override public void run() {
      updateThreads();
    }
  };
}
