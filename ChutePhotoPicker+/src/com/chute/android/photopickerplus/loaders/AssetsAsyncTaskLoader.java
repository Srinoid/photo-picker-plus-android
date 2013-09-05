package com.chute.android.photopickerplus.loaders;

import android.content.Context;
import android.database.Cursor;

import com.chute.android.photopickerplus.dao.MediaDAO;
import com.chute.android.photopickerplus.util.PhotoFilterType;

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
