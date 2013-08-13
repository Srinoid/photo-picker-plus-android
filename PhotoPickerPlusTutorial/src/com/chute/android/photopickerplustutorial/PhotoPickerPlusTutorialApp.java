/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplustutorial;

import java.util.ArrayList;

import com.chute.android.photopickerplus.PhotoPickerPlusApp;
import com.chute.android.photopickerplus.config.ConfigServicesFactory;
import com.chute.android.photopickerplus.util.Constants;
import com.chute.sdk.v2.model.AccountStore;

public class PhotoPickerPlusTutorialApp extends PhotoPickerPlusApp {

	private ArrayList<String> services = new ArrayList<String>();

	@Override
	public void onCreate() {
		super.onCreate();
		AccountStore.setAppId(getApplicationContext(), Constants.APP_ID);
		services.add("Facebook");
		services.add("Picasa");
		services.add("Instagram");
		services.add("Take photo");
		services.add("Last taken photo");
		services.add("Camera shots");
		services.add("All photos");
		

		/**
		 * Call for managing services locally.
		 **/
		 ConfigServicesFactory.getInstance(getApplicationContext()).configureServices(
		 ConfigServicesFactory.CONFIG_LOCAL, services);
		/**
		 * Get which services need to be used from the server; Important: Save
		 * endpoint URL in PhotoPicker+ preferences.
		 **/
//		PhotoPickerPreferenceUtil.get().setConfigUrl(ConfigEndpointURLs.SERVICES_ENDPOINT_URL);
//		ConfigServicesFactory.getInstance(getApplicationContext()).configureServices(
//				ConfigServicesFactory.CONFIG_SERVER, null);

	}

}
