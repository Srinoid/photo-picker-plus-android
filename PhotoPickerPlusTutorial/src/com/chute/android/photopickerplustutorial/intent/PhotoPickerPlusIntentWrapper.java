package com.chute.android.photopickerplustutorial.intent;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.chute.android.photopickerplus.ui.activity.ServicesActivity;

public class PhotoPickerPlusIntentWrapper extends IntentWrapper {

	public static final int REQUEST_CODE = 1;
	public static final String TAG = PhotoPickerPlusIntentWrapper.class.getSimpleName();
	public static final String FLAG_MULTI_PP = "flagMultiPp";
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

	public boolean getIsMultiPicker() {
		return getIntent().getExtras().getBoolean(FLAG_MULTI_PP);
	}

	public void setServiceList(ArrayList<String> services) {
		getIntent().putStringArrayListExtra(FLAG_SERVICE_LIST, services);
	}

	public ArrayList<String> getServiceList() {
		return getIntent().getExtras().getStringArrayList(FLAG_SERVICE_LIST);
	}

	public void setMultiPicker(boolean flag) {
		getIntent().putExtra(FLAG_MULTI_PP, flag);
	}

	public void startActivityForResult(Activity context, int code) {
		context.startActivityForResult(getIntent(), code);
	}

}
