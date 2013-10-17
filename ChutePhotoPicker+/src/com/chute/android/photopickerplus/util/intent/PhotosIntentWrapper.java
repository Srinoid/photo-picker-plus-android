/**
 * The MIT License (MIT)

Copyright (c) 2013 Chute

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.util.intent;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.chute.android.photopickerplus.ui.activity.AssetActivity;
import com.chute.android.photopickerplus.models.enums.PhotoFilterType;
import com.chute.sdk.v2.model.AccountModel;
import com.chute.sdk.v2.model.AssetModel;

/**
 * {@link PhotosIntentWrapper} is a wrapper class that wraps the following
 * parameters needed for the intent:
 * <ul>
 * <li> {@link AccountModel}
 * <li>Album ID
 * <li>List of {@link AssetModel}s
 * <li>{@link PhotoFilterType}
 * </ul>
 * 
 */
public class PhotosIntentWrapper extends IntentWrapper {

  public static final String TAG = PhotosIntentWrapper.class.getSimpleName();

  public static final int ACTIVITY_FOR_RESULT_PHOTO_KEY = 115;
  public static final int ACTIVITY_FOR_RESULT_STREAM_KEY = 113;

  // social photos
  private static final String KEY_ACCOUNT = "account";
  private static final String KEY_ALBUM_ID = "albumId";
  private static final String KEY_PHOTO_COLLECTION = "photoCollection";

  private static final String EXTRA_KEY_FILTER_TYPE = "filter_type";

  public PhotosIntentWrapper(Context context) {
    super(context, AssetActivity.class);
  }

  public PhotosIntentWrapper(Intent intent) {
    super(intent);
  }

  public AccountModel getAccount() {
    return getIntent().getExtras().getParcelable(KEY_ACCOUNT);
  }

  public void setAccount(AccountModel account) {
    getIntent().putExtra(KEY_ACCOUNT, account);
  }

  public String getAlbumId() {
    return getIntent().getExtras().getString(KEY_ALBUM_ID);
  }

  public void setAlbumId(String albumId) {
    getIntent().putExtra(KEY_ALBUM_ID, albumId);
  }

  public ArrayList<AssetModel> getMediaCollection() {
    return getIntent().getExtras().getParcelable(KEY_PHOTO_COLLECTION);
  }

  public void setMediaCollection(ArrayList<AssetModel> mediaCollection) {
    getIntent().putExtra(KEY_PHOTO_COLLECTION, (Parcelable) mediaCollection);
  }

  public PhotoFilterType getFilterType() {
    return (PhotoFilterType) getIntent().getExtras().get(EXTRA_KEY_FILTER_TYPE);
  }

  public void setFilterType(PhotoFilterType type) {
    getIntent().putExtra(EXTRA_KEY_FILTER_TYPE, type);
  }

  public void startActivityForResult(Activity context, int code) {
    context.startActivityForResult(getIntent(), code);
  }
}
