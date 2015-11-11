package com.karumi.trabajandoendiferido;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import trabajandoendiferido.karumi.com.trabajandoendiferido.R;

public class MainActivity extends Activity {

  private TextView numThreadView;
  private TextView threadsView;

  Handler handler = new Handler();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mapUi();
  }

  private void mapUi() {
    numThreadView = ((TextView) findViewById(R.id.tv_num_thread));
    threadsView = ((TextView) findViewById(R.id.tv_threads));
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
      threadsText += thread.getName() + "/\n";
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
