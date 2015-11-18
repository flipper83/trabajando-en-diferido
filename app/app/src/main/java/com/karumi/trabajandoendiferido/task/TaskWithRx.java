package com.karumi.trabajandoendiferido.task;

import com.karumi.trabajandoendiferido.api.ApiCall;
import com.karumi.trabajandoendiferido.api.response.ApiResponse;
import com.karumi.trabajandoendiferido.ui.Ui;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.FuncN;
import rx.internal.util.RxThreadFactory;
import rx.schedulers.Schedulers;

/**
 *
 */
public class TaskWithRx implements Task {
  private final ApiCall apiCall;

  public TaskWithRx(ApiCall apiCall) {
    this.apiCall = apiCall;
  }

  @Override public void executeTask(final Ui ui, int totalTask) {
    List<Observable<ApiResponse>> calls = new ArrayList<>();
    for (int i = 0; i < totalTask; i++) {
      Observable<ApiResponse> apiResponseObservable = apiCall.callObservable(i + 1);
      calls.add(apiResponseObservable);
    }

    Observable.zip(calls, new FuncN<Long>() {
      @Override public Long call(Object... args) {
        return System.currentTimeMillis();
      }
    })
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
      @Override public void call(Long time) {
        ui.showTime(time);
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        ui.showError("error " + throwable);
      }
    });
  }
}
