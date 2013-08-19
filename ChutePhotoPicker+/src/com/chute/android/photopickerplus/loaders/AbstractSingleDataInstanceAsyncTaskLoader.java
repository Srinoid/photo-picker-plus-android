package com.chute.android.photopickerplus.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.araneaapps.android.libs.logger.ALog;

/**
 * @author darko.grozdanovski
 * @param <D>
 */
public abstract class AbstractSingleDataInstanceAsyncTaskLoader<D> extends
    AsyncTaskLoader<D> {

  private D data;

  public AbstractSingleDataInstanceAsyncTaskLoader(Context context) {
    super(context);

  }

  public static final String TAG = AbstractSingleDataInstanceAsyncTaskLoader.class
      .getSimpleName();

  @Override
  public void deliverResult(D data) {
    if (isReset()) {
      // An asynchronous query came in while the loader is stopped. We
      // don't need the result.
      if (data != null) {
        onReleaseResources(data);
      }
    }
    this.data = data;
    if (isStarted()) {
      // If the Loader is currently started, we can immediately
      // deliver its results.
      super.deliverResult(data);
    }

  }

  public void changeStoredResources(D data) {
    this.data = data;
  }

  public D getData() {
    return data;
  }

  private void onReleaseResources(D data) {
    // TODO Auto-generated method stub
  }

  /**
   * Handles a request to start the Loader.
   */
  @Override
  protected void onStartLoading() {
    if (data != null) {
      // If we currently have a result available, deliver it
      // immediately.
      deliverResult(data);
    }

    if (takeContentChanged() || data == null) {
      // If the data has changed since the last time it was loaded
      // or is not currently available, start a load.
      forceLoad();
    }
  }

  @Override
  public void forceLoad() {
    ALog.d(TAG, "forcing load");
    super.forceLoad();
  }

  /**
   * Handles a request to stop the Loader.
   */
  @Override
  protected void onStopLoading() {
    // Attempt to cancel the current load task if possible.
    cancelLoad();
  }

  @Override
  public void onCanceled(D data) {
    super.onCanceled(data);
    onReleaseResources(data);
  }

  /**
   * Handles a request to completely reset the Loader.
   */
  @Override
  protected void onReset() {
    super.onReset();

    // Ensure the loader is stopped
    onStopLoading();

    // At this point we can release the resources associated with 'apps'
    // if needed.
    if (data != null) {
      onReleaseResources(data);
      data = null;
    }
  }
}
