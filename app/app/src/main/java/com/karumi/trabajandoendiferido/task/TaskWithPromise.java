package com.karumi.trabajandoendiferido.task;

import com.karumi.trabajandoendiferido.api.ApiCall;
import com.karumi.trabajandoendiferido.api.response.ApiResponse;
import com.karumi.trabajandoendiferido.ui.Ui;
import java.io.IOException;
import org.jdeferred.DeferredManager;
import org.jdeferred.Promise;
import org.jdeferred.android.AndroidDoneCallback;
import org.jdeferred.android.AndroidExecutionScope;
import org.jdeferred.android.AndroidFailCallback;
import org.jdeferred.impl.DefaultDeferredManager;
import org.jdeferred.impl.DeferredObject;
import org.jdeferred.multiple.MultipleResults;
import org.jdeferred.multiple.OneReject;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 *
 */
public class TaskWithPromise implements Task {
  private final ApiCall apiCall;

  public TaskWithPromise(ApiCall apiCall) {
    this.apiCall = apiCall;
  }

  @Override public void executeTask(final Ui ui, int totalTask) {
    DeferredManager dm = new DefaultDeferredManager();

    Promise[] promises = new Promise[totalTask];

    for (int i = 0; i < totalTask; i++) {
      DeferredApiCall deferredApiCall = new DeferredApiCall(apiCall, i + 1);
      promises[i] = deferredApiCall.getPromise();
      deferredApiCall.callApi();
    }

    dm.when(promises).done(new AndroidDoneCallback<MultipleResults>() {
      @Override public AndroidExecutionScope getExecutionScope() {
        return AndroidExecutionScope.UI;
      }

      @Override public void onDone(MultipleResults result) {
        ui.showTime(System.currentTimeMillis());
      }
    }).fail(new AndroidFailCallback<OneReject>() {
      @Override public AndroidExecutionScope getExecutionScope() {
        return AndroidExecutionScope.UI;
      }

      @Override public void onFail(OneReject result) {
        ui.showError(result.toString());
      }
    });
  }

  private class DeferredApiCall {
    private final ApiCall apiCall;
    private final int call;
    private DeferredObject<String, Throwable, Integer> deferredObject = new DeferredObject<>();

    public DeferredApiCall(ApiCall apiCall, int call) {
      this.apiCall = apiCall;
      this.call = call;
    }

    public void callApi() {
        apiCall.callAsync(call, new Callback<ApiResponse>() {
          @Override public void onResponse(Response<ApiResponse> response, Retrofit retrofit) {
            deferredObject.resolve("done");
          }

          @Override public void onFailure(Throwable t) {
            deferredObject.reject(t);
          }
        });
    }

    public Promise getPromise() {
      return deferredObject.promise();
    }
  }
}
