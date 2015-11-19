package com.karumi.trabajandoendiferido.task.job;

import com.karumi.trabajandoendiferido.ui.Ui;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

/**
 *
 */
public abstract class BaseJob extends Job{

  protected BaseJob() {
    super(new Params(1));
  }

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    execute();
  }

  @Override protected void onCancel() {

  }


  protected abstract void execute();
}
