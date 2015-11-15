package com.karumi.trabajandoendiferido;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.karumi.trabajandoendiferido.api.ApiCall;
import com.karumi.trabajandoendiferido.api.response.ApiResponse;
import com.karumi.trabajandoendiferido.server.MockApiCalls;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import trabajandoendiferido.karumi.com.trabajandoendiferido.R;

public class MainActivity extends Activity {

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

    timeSending = System.currentTimeMillis();
    setTimeLoading();

    if (apiCall == null) {
      apiCall = new ApiCall(mockApiCalls);
      apiCall.init();
    }

    Call<ApiResponse> call = apiCall.callOneAsync();

    call.enqueue(new Callback<ApiResponse>() {
      @Override public void onResponse(Response<ApiResponse> response, Retrofit retrofit) {
        int code = response.code();
        if (code == 200) {
          long timeDiff = System.currentTimeMillis() - timeSending;
          setTime("" + timeDiff);
        } else {
          setTime("error " + code);
        }
      }

      @Override public void onFailure(Throwable t) {
        setTime("error " + t);
      }
    });
  }

  private void setTimeLoading() {
    String time = getString(R.string.time_title) + " " + getString(R.string.loading);
    timeView.setText(time);
  }

  private void setTime(String time) {
    String timeComposed = getString(R.string.time_title) + " " + time;
    timeView.setText(timeComposed);
  }

  @Override protected void onResume() {
    super.onResume();
    updateThreads();
    // in on resume only exist 3 threads, binder and main.
  }

  private void updateThreads() {
    int count = Thread.activeCount();
    numThreadView.setText(count + " threads");

    Thread[] threads = new Thread[count];
    Thread.enumerate(threads);
    String threadsText = "";
    for (Thread thread : threads) {
      if (thread != null) {
        threadsText += thread.getName() + "/\n";
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
