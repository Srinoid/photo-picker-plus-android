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

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.chute.android.photopickerplus.ui.activity.ServicesActivity;
import com.chute.sdk.v2.model.AssetModel;

/**
 * Helper class that contains methods for delivering the result to the main
 * activity i.e. the activity that started the PhotoPicker.
 * 
 */
public class IntentUtil {

  public static void deliverDataToInitialActivity(final FragmentActivity context,
      final AssetModel model) {
    ArrayList<AssetModel> mediaCollection = new ArrayList<AssetModel>();
    mediaCollection.add(model);
    deliverDataToInitialActivity(context, mediaCollection);
  }

  public static void deliverDataToInitialActivity(final FragmentActivity context,
      final ArrayList<AssetModel> collection) {
    final PhotoPickerPlusIntentWrapper wrapper = new PhotoPickerPlusIntentWrapper(new Intent(
        context,
        ServicesActivity.class));
    wrapper.setMediaCollection(collection);
    wrapper.getIntent().addFlags(
        Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    wrapper.startActivity(context);
  }
}
