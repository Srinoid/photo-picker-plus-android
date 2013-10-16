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
package com.chute.android.photopickerplustutorial.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.chute.android.photopickerplus.ui.activity.ServicesActivity;

public class PhotoPickerPlusTutorialIntentWrapper extends IntentWrapper {

  public static final int REQUEST_CODE = 189;
  public static final String TAG = PhotoPickerPlusTutorialIntentWrapper.class.getSimpleName();

  public PhotoPickerPlusTutorialIntentWrapper(Intent intent) {
    super(intent);
  }

  public PhotoPickerPlusTutorialIntentWrapper(Context packageContext, Class<?> cls) {
    super(packageContext, cls);
  }

  public PhotoPickerPlusTutorialIntentWrapper(Context packageContext) {
    super(new Intent(packageContext, ServicesActivity.class));
  }

  public void startActivityForResult(Activity context, int code) {
    context.startActivityForResult(getIntent(), code);
  }

}
