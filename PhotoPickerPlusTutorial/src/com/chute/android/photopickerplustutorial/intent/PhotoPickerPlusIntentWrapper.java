package com.chute.android.photopickerplustutorial.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.chute.android.photopickerplus.ui.activity.ServicesActivity;

public class PhotoPickerPlusIntentWrapper extends IntentWrapper {

  public static final int REQUEST_CODE = 1;
  public static final String TAG = PhotoPickerPlusIntentWrapper.class.getSimpleName();
  public static final String FLAG_SERVICE_LIST = "flagServiceList";

  public PhotoPickerPlusIntentWrapper(Intent intent) {
    super(intent);
  }

  public PhotoPickerPlusIntentWrapper(Context packageContext, Class<?> cls) {
    super(packageContext, cls);
  }

  public PhotoPickerPlusIntentWrapper(Context packageContext) {
    super(new Intent(packageContext, ServicesActivity.class));
  }


  public void startActivityForResult(Activity context, int code) {
    context.startActivityForResult(getIntent(), code);
  }

}
