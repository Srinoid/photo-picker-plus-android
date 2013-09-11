/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

public class PhotosIntentWrapper extends IntentWrapper {

  public static final String TAG = PhotosIntentWrapper.class.getSimpleName();

  public static final int ACTIVITY_FOR_RESULT_PHOTO_KEY = 115;
  public static final int ACTIVITY_FOR_RESULT_STREAM_KEY = 113;

  // social photos
  private static final String KEY_ACCOUNT = "account";
  private static final String KEY_ALBUM_ID = "albumId";
  private static final String KEY_PHOTO_COLLECTION = "photoCollection";

  // cursor photos
  private static final String EXTRA_KEY_PATH_LIST = "key_path_list";
  private static final String EXTRA_KEY_PATH = "key_path";
  private static final String EXTRA_KEY_CURSOR_PHOTOS = "cursor_photos";

  // types
  public static final int TYPE_CAMERA_ROLL = 0;
  public static final int TYPE_ALL_PHOTOS = 1;
  public static final int TYPE_SOCIAL_PHOTOS = 2;

  private static final String EXTRA_KEY_CHUTE_ID = "key_chute_id";

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

  public void setAssetPathList(ArrayList<String> pathList) {
    getIntent().putStringArrayListExtra(EXTRA_KEY_PATH_LIST, pathList);
  }

  public ArrayList<String> getAssetPathList() {
    return getIntent().getExtras().getStringArrayList(EXTRA_KEY_PATH_LIST);
  }

  public void setAssetPath(String path) {
    getIntent().putExtra(EXTRA_KEY_PATH, path);
  }

  public String getAssetPath() {
    return getIntent().getExtras().getString(EXTRA_KEY_PATH);
  }

  public PhotoFilterType getFilterType() {
    return (PhotoFilterType) getIntent().getExtras().get(EXTRA_KEY_CURSOR_PHOTOS);
  }

  public void setFilterType(PhotoFilterType type) {
    getIntent().putExtra(EXTRA_KEY_CURSOR_PHOTOS, type);
  }

  public String getChuteId() {
    return getIntent().getExtras().getString(EXTRA_KEY_CHUTE_ID);
  }

  public void setChuteId(String id) {
    getIntent().putExtra(EXTRA_KEY_CHUTE_ID, id);
  }

  public void startActivityForResult(Activity context, int code) {
    context.startActivityForResult(getIntent(), code);
  }
}
