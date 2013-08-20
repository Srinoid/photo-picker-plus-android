/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplustutorial.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

import com.chute.android.photopickerplus.util.intent.GridActivityIntentWrapper;
import com.chute.android.photopickerplustutorial.R;
import com.chute.android.photopickerplustutorial.adapter.GridAdapter;
import com.chute.android.photopickerplustutorial.intent.PhotoPickerPlusIntentWrapper;
import com.chute.sdk.v2.model.AccountMediaModel;

public class PhotoPickerPlusTutorialActivity extends FragmentActivity {

  public static final String TAG = PhotoPickerPlusTutorialActivity.class.getSimpleName();
  private static final String KEY_SELECTED_ITEMS = "keySelectedItems";
  private GridView grid;
  /**
   * PhotoPicker+ component enables choosing multiple photos or a single photo
   * in a grid. If you wish to enable multi-picking functionality, set
   * isMultipicker=true, otherwise set isMultiPicker=false.
   */
  private final boolean isMultiPicker = true;
  private GridAdapter adapter;
  private ArrayList<AccountMediaModel> accountMediaList;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_photo_picker_plus);

    findViewById(R.id.btnPhotoPicker)
        .setOnClickListener(new OnPhotoPickerClickListener());
    grid = (GridView) findViewById(R.id.grid);
    if (savedInstanceState != null) {
      accountMediaList = savedInstanceState.getParcelableArrayList(KEY_SELECTED_ITEMS);
      adapter = new GridAdapter(PhotoPickerPlusTutorialActivity.this, accountMediaList);
    } else {
      adapter = new GridAdapter(PhotoPickerPlusTutorialActivity.this,
          new ArrayList<AccountMediaModel>());
    }
    grid.setAdapter(adapter);

    int orientation = getResources().getConfiguration().orientation;
    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
      grid.setNumColumns(5);
    }

  }

  private class OnPhotoPickerClickListener implements OnClickListener {

    @Override
    public void onClick(View v) {
      PhotoPickerPlusIntentWrapper wrapper = new PhotoPickerPlusIntentWrapper(
          PhotoPickerPlusTutorialActivity.this);
      wrapper.setMultiPicker(isMultiPicker);
      wrapper.startActivityForResult(PhotoPickerPlusTutorialActivity.this,
          PhotoPickerPlusIntentWrapper.REQUEST_CODE);

    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != Activity.RESULT_OK) {
      return;
    }
    final GridActivityIntentWrapper wrapper = new GridActivityIntentWrapper(data);
    accountMediaList = wrapper.getMediaCollection();
    adapter.changeData(accountMediaList);
    // Log.d("debug", wrapper.getMediaCollection().toString());

    // String path;
    // Uri uri = Uri.parse(wrapper.getMediaCollection().get(0).getUrl());
    // if (uri.getScheme().contentEquals("http")) {
    // path = uri.toString();
    // } else {
    // path = uri.getPath();
    // }
    // ALog.d("The Path or url of the file " + path);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putParcelableArrayList(KEY_SELECTED_ITEMS, accountMediaList);
    super.onSaveInstanceState(outState);
  }
}
