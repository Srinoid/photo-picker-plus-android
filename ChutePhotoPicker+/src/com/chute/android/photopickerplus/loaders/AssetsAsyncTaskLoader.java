package com.chute.android.photopickerplus.loaders;

import android.content.Context;
import android.database.Cursor;

import com.chute.android.photopickerplus.dao.MediaDAO;
import com.chute.android.photopickerplus.util.intent.PhotosIntentWrapper;


public class AssetsAsyncTaskLoader extends
		AbstractSingleDataInstanceAsyncTaskLoader<Cursor> {

	public static final String TAG = AssetsAsyncTaskLoader.class
			.getSimpleName();
	private final int filterType;

	public AssetsAsyncTaskLoader(Context context, int filterType) {
		super(context);
		this.filterType = filterType;
	}

	@Override
	public Cursor loadInBackground() {
		if (filterType == PhotosIntentWrapper.TYPE_ALL_PHOTOS) {
			return MediaDAO.getAllMediaPhotos(getContext());
		} else if (filterType == PhotosIntentWrapper.TYPE_CAMERA_ROLL) {
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
