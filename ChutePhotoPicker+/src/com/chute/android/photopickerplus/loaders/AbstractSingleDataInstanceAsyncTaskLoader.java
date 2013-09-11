/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.araneaapps.android.libs.logger.ALog;

/**
 * An abstract class that performs asynchronous loading of data. While Loaders
 * are active they should monitor the source of their data and deliver new
 * results when the contents change.
 * 
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
      /*
       * An asynchronous query came in while the loader is stopped. The result
       * is not needed.
       */
      if (data != null) {
        onReleaseResources(data);
      }
    }
    this.data = data;
    if (isStarted()) {
      /*
       * If the Loader is currently started, the results can be delivered
       * immediately.
       */
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
      /*
       * If the result is currently available, it should be delivered
       * immediately.
       */
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
    /* Attempt to cancel the current load task if possible. */
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

    /* Ensure the loader is stopped */
    onStopLoading();

    /*
     * At this point we can release the resources associated with 'apps' if
     * needed.
     */
    if (data != null) {
      onReleaseResources(data);
      data = null;
    }
  }
}
