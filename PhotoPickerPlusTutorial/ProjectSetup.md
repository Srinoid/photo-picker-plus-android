
Introduction to Chute
====

This document will help you successfully set up the basic things that are required for
getting started with Chute.

You can download the Chute SDK for Android at:
[https://github.com/chute/Chute-SDK-V2-Android](https://github.com/chute/Chute-SDK-V2-Android).

Additionally you can visit [http://developer.getchute.com/](http://developer.getchute.com/) for more information about creating a new developer account and obtaining chute credentials.

You can create a Chute developer account and make a new app in Chute at [http://apps.getchute.com/](http://apps.getchute.com/)

- For URL you can enter http://getchute.com/ if you don't have a site for your app.
- For Callback URL you can use http://getchute.com/oauth/callback if you don't need callbacks for another purpose.
	
	![image1](https://raw.github.com/chute/photo-picker-plus/v2-photopickerplus/Android/PhotoPickerPlusTutorial/screenshots/1.png)![image2](https://raw.github.com/chute/photo-picker-plus/v2-photopickerplus/Android/PhotoPickerPlusTutorial/screenshots/2.png)  

Basic SDK Setup
====

* Download the SDK 
* In Eclipse click File -> Import -> Maven -> Existing Maven Projects

![image3](https://raw.github.com/chute/photo-picker-plus/v2-photopickerplus/Android/PhotoPickerPlusTutorial/screenshots/3.png)

* Browse and select the downloaded root directory of the SDK
* Check the SDK project and click Finish


Basic New Project Setup
====

* Open Eclipse and create new Android project by selecting File->New->Android Application Project.
* Type in the name of the project and specify the SDK versions.

       ![image4](https://raw.github.com/chute/photo-picker-plus/v2-photopickerplus/Android/PhotoPickerPlusTutorial/screenshots/4.png)![image5](https://raw.github.com/chute/photo-picker-plus/v2-photopickerplus/Android/PhotoPickerPlusTutorial/screenshots/5.png)
  
* Create a laucher icon.
 
       ![image6](https://raw.github.com/chute/photo-picker-plus/v2-photopickerplus/Android/PhotoPickerPlusTutorial/screenshots/6.png)
  
* Create the main activity and finish the setup.

       ![image7](https://raw.github.com/chute/photo-picker-plus/v2-photopickerplus/Android/PhotoPickerPlusTutorial/screenshots/7.png)![image8](https://raw.github.com/chute/photo-picker-plus/v2-photopickerplus/Android/PhotoPickerPlusTutorial/screenshots/8.png)
  
Adding the SDK library to your project
====

* After successfully creating a new Android project or opening an existing one, the next thing that needs to be done
  is adding the Chute SDK as a library.
* Chute SDK project can be found and downloaded at [https://github.com/chute/photo-picker-plus/tree/master/Android/SDK](https://github.com/chute/photo-picker-plus/tree/master/Android/SDK). Or visit [http://developer.getchute.com/](http://developer.getchute.com/) for more info.

- You can add Chute SDK library either by:
* Coping the jar file located in target/ directory of the Chute SDK library project into libs/ directory or your project
  or
* adding Chute SDK library project in your project's build path by selecting Properties -> Java Build Path -> Add

       ![image9](https://raw.github.com/chute/photo-picker-plus/v2-photopickerplus/Android/PhotoPickerPlusTutorial/screenshots/9.png)
  
    
Android manifest setup
====

* Open the AndroidManifest.xml file 

* Add the following permissions that are required for the Chute SDK:

    ```
        Standard Permission to access the internet
        <uses-permission android:name="android.permission.INTERNET" />
        Permission for caching the images on the SD card
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        The following permissions are used by chute to track the phone id and version to determine the device used for managing the uploads and users
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
        <uses-permission android:name="android.permission.READ_PHONE_STATE" />
        <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
        <uses-permission android:name="android.permission.GET_ACCOUNTS" />
        Used for the http request service for the long running uploads.
        <uses-permission android:name="android.permission.WAKE_LOCK" />
    ```

Apart from registering your own components in the manifest, you will need to add the following service and activity also:

* Register HttpServiceExecutorService:

    ```
        <service android:name="com.dg.libs.rest.services.HTTPRequestExecutorService" />
    ```
 
* Register AuthenticationActivity from Chute SDK:

    ```
        <activity android:name="com.chute.sdk.api.v2.authentication.GCAuthenticationActivity" />
    ```
 
