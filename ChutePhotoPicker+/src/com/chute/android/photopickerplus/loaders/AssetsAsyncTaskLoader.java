/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.loaders;

import android.content.Context;
import android.database.Cursor;

import com.chute.android.photopickerplus.dao.MediaDAO;
import com.chute.android.photopickerplus.models.enums.PhotoFilterType;

/**
 * The {@link AssetsAsyncTaskLoader} class is an AsyncTaskLoader subclass that
 * loads photos that can be found on the device.
 */
public class AssetsAsyncTaskLoader extends
    AbstractSingleDataInstanceAsyncTaskLoader<Cursor> {

  public static final String TAG = AssetsAsyncTaskLoader.class
      .getSimpleName();
  private final PhotoFilterType filterType;

  public AssetsAsyncTaskLoader(Context context, PhotoFilterType filterType) {
    super(context);
    this.filterType = filterType;
  }

  @Override
  public Cursor loadInBackground() {
    if (filterType == PhotoFilterType.ALL_PHOTOS) {
      return MediaDAO.getAllMediaPhotos(getContext());
    } else if (filterType == PhotoFilterType.CAMERA_ROLL) {
      return MediaDAO.getCameraPhotos(getContext());
    } else {
      return null;
    }
  }

  @Override
  public void deliverResult(Cursor data) {
    super.deliverResult(data);
  }

}
