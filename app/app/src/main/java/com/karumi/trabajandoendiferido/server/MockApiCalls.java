package com.karumi.trabajandoendiferido.server;

import android.util.Log;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

/**
 *
 */
public class MockApiCalls {
  public static final int OK_STATUS = 200;
  private static final String LOGTAG = "MockApiCalls";
  MockWebServer mockWebServer;

  public MockApiCalls() {
    this.mockWebServer = new MockWebServer();
  }

  public void init() {
    new Thread(new Runnable() {
      public void run() {
        mockWebServer.setDispatcher(new Dispatcher() {
          @Override public MockResponse dispatch(RecordedRequest request)
              throws InterruptedException {
            if (request.getPath().equals("/1/")) {
              Thread.sleep(2000);
              return new MockResponse().setResponseCode(OK_STATUS).setBody("{\"value\":\"1\"}");
            } else if (request.getPath().equals("/2/")) {
              Thread.sleep(3000);
              return new MockResponse().setResponseCode(OK_STATUS).setBody("{\"value\":\"2\"}");
            } else if (request.getPath().equals("/3/")) {
              Thread.sleep(4000);
              return new MockResponse().setResponseCode(OK_STATUS).setBody("{\"value\":\"3\"}");
            }
            return new MockResponse().setResponseCode(404);
          }
        });

        try {
          mockWebServer.start();
        } catch (Exception e) {
          Log.e(LOGTAG, "ERROR ", e);
        }
      }
    }, "serverThread").start();
  }

  public String getUrl() {
    return mockWebServer.url("/").toString();
  }
}
