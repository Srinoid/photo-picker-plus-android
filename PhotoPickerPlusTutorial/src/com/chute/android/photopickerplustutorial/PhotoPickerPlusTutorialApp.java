/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplustutorial;

import com.chute.android.photopickerplus.PhotoPickerPlusApp;
import com.chute.android.photopickerplus.config.ConfigServicesSingleton;
import com.chute.android.photopickerplus.models.enums.LocalMediaType;
import com.chute.sdk.v2.api.Chute;
import com.chute.sdk.v2.api.authentication.AuthConstants;
import com.chute.sdk.v2.model.enums.AccountType;

public class PhotoPickerPlusTutorialApp extends PhotoPickerPlusApp {

  public static final String APP_ID = "4f3c39ff38ecef0c89000003";
  public static final String APP_SECRET = "c9a8cb57c52f49384ab6117c4f6483a1a5c5a14c4a50d4cef276a9a13286efc9";


  @Override
  public void onCreate() {
    super.onCreate();
    Chute.init(this, new AuthConstants(APP_ID, APP_SECRET));

    ConfigServicesSingleton.getInstance(getApplicationContext()).setAvailableLocalServices(LocalMediaType.ALL_PHOTOS, LocalMediaType.TAKE_PHOTO);
    ConfigServicesSingleton.getInstance(getApplicationContext()).setAvailableRemoteServices(AccountType.FACEBOOK, AccountType.INSTAGRAM);
    
    /**
     * Get which services need to be used from the server; Important: Save
     * endpoint URL in PhotoPicker+ preferences.
     **/
    // PhotoPickerPreferenceUtil.get()
    // .setConfigUrl(ConfigEndpointURLs.SERVICES_ENDPOINT_URL);
    // ConfigServicesSingleton.getInstance(getApplicationContext()).fetchConfigFromServer();

  }

}
