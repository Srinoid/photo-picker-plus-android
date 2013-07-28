/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus;

import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.util.TypedValue;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.config.ConfigServicesFactory;
import com.chute.android.photopickerplus.util.Constants;
import com.chute.android.photopickerplus.util.PhotoPickerPreferenceUtil;
import com.chute.sdk.v2.api.Chute;
import com.chute.sdk.v2.api.authentication.AuthConstants;
import com.chute.sdk.v2.utils.PreferenceUtil;

import darko.imagedownloader.ImageLoader;

public class PhotoPickerPlusApp extends Application {

	public static final String TAG = PhotoPickerPlusApp.class.getSimpleName();

	private static ImageLoader createImageLoader(Context context) {
		ImageLoader imageLoader = new ImageLoader(context, R.drawable.placeholder);
		imageLoader.setDefaultBitmapSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, context
				.getResources().getDisplayMetrics()));
		return imageLoader;
	}

	private ImageLoader mImageLoader;
	private ArrayList<String> services = new ArrayList<String>();

	@Override
	public void onCreate() {
		super.onCreate();
		mImageLoader = createImageLoader(this);
		Chute.init(this, new AuthConstants(Constants.APP_ID, Constants.APP_SECRET));
		PreferenceUtil.init(getApplicationContext());
		PhotoPickerPreferenceUtil.init(getApplicationContext());
		services.add("Facebook");
		services.add("Picasa");
		ConfigServicesFactory.getInstance(getApplicationContext()).configureServices(
				ConfigServicesFactory.CONFIG_LOCAL, services);
		// ConfigServicesFactory.getInstance(getApplicationContext()).configureServices(ConfigServicesFactory.CONFIG_SERVER,
		// null);
	}

	@Override
	public Object getSystemService(String name) {
		if (ImageLoader.IMAGE_LOADER_SERVICE.equals(name)) {
			return mImageLoader;
		} else {
			return super.getSystemService(name);
		}
	}

}
