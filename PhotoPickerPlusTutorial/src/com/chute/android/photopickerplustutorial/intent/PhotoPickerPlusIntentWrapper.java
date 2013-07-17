package com.chute.android.photopickerplustutorial.intent;

import com.chute.android.photopickerplus.ui.activity.ChooseServiceActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class PhotoPickerPlusIntentWrapper extends IntentWrapper {

	public static final int REQUEST_CODE = 1;
	public static final String TAG = PhotoPickerPlusIntentWrapper.class.getSimpleName();
	public static final String FLAG_MULTI_PP = "flagMultiPp";

	public PhotoPickerPlusIntentWrapper(Intent intent) {
		super(intent);
	}

	public PhotoPickerPlusIntentWrapper(Context packageContext, Class<?> cls) {
		super(packageContext, cls);
	}

	public PhotoPickerPlusIntentWrapper(Context packageContext) {
		super(new Intent(packageContext, ChooseServiceActivity.class));
	}

	public boolean getIsMultiPicker() {
		return getIntent().getExtras().getBoolean(FLAG_MULTI_PP);
	}

	public void setMultiPicker(boolean flag) {
		getIntent().putExtra(FLAG_MULTI_PP, flag);
	}

	public void startActivityForResult(Activity context, int code) {
		context.startActivityForResult(getIntent(), code);
	}

}
