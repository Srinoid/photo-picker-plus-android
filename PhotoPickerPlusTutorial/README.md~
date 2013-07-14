Introduction
====

PhotoPickerPlusTutorial is a tutorial project that shows how to use the PhotoPicker+ component. It contains Chute SDK library as well as PhotoPicker+ library. This tutorial enables browsing albums and photos on your social services and device gallery, selecting a photo and display of the selected photo and its data.

![image10](https://raw.github.com/chute/photo-picker-plus/v2-photopickerplus/Android/PhotoPickerPlusTutorial/screenshots/10.png)![image11](https://raw.github.com/chute/photo-picker-plus/v2-photopickerplus/Android/PhotoPickerPlusTutorial/screenshots/11.png)![image12](https://raw.github.com/chute/photo-picker-plus/v2-photopickerplus/Android/PhotoPickerPlusTutorial/screenshots/12.png)![image13](https://raw.github.com/chute/photo-picker-plus/v2-photopickerplus/Android/PhotoPickerPlusTutorial/screenshots/13.png)![image14](https://raw.github.com/chute/photo-picker-plus/v2-photopickerplus/Android/PhotoPickerPlusTutorial/screenshots/14.png)

Setup
====

* Add the PhotoPicker+ component to your project by either copying all the resources and source code or by adding it as an Android Library project.

* Add Chute SDK library by selecting Properties -> Java Build Path -> Libraries -> Add JARs and choose the jar located in PhotoPicker+ component libs/ direcotry. Select Order and Export tab and put a check on the lib you just added.

* Go through [ProjectSetup.md](https://github.com/chute/photo-picker-plus/blob/master/Android/PhotoPickerPlusTutorial/ProjectSetup.md) for more info.

   ![image15](https://raw.github.com/chute/photo-picker-plus/v2-photopickerplus/Android/PhotoPickerPlusTutorial/screenshots/15.png)![image16](https://raw.github.com/chute/photo-picker-plus/v2-photopickerplus/Android/PhotoPickerPlusTutorial/screenshots/16.png)


* Register the activities, services and the application class into AndroidManifest.xml file:

    ```
        <application
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name"
        android:name=".app.PhotoPickerPlusTutorialApp"
        android:theme="@android:style/Theme.Light.NoTitleBar" >
		
         <service android:name="com.dg.libs.rest.services.HTTPRequestExecutorService" />

        <activity
            android:label="@string/app_name"
            android:name=".app.PhotoPickerPlusTutorialActivity"
            android:screenOrientation="portrait"
            android:theme="@android:style/Theme.Light.NoTitleBar" >
            <intent-filter >
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity
            android:name="com.chute.android.photopickerplus.app.ChooseServiceActivity"
            android:screenOrientation="portrait"
            android:theme="@android:style/Theme.Light.NoTitleBar" >
        </activity>
        <activity
            android:name="com.chute.android.photopickerplus.app.AlbumsActivity"
            android:screenOrientation="portrait"
            android:theme="@android:style/Theme.Light.NoTitleBar" >
        </activity>
        <activity
            android:name="com.chute.sdk.v2.api.authentication.AuthenticationActivity"
            android:theme="@android:style/Theme.Light.NoTitleBar" >
        </activity>
        <activity
            android:name="com.chute.android.photopickerplus.app.PhotoStreamActivity"
            android:screenOrientation="portrait"
            android:theme="@android:style/Theme.Light.NoTitleBar" >
        </activity>
        <activity
            android:name="com.chute.android.photopickerplus.app.PhotosActivity"
            android:screenOrientation="portrait"
            android:theme="@android:style/Theme.Light.NoTitleBar" >
        </activity>
        </application>
    ```


Usage
====

##PhotoPickerPlusTutorialApp.java 
This class is the extended Application class. It is registered inside the "application" tag in the manifest and is used for initializing the utility classes used in the component.
PhotoPickerPlusTutorialApp can extend PhotoPickerPlusApp like shown in this tutorial:

<pre><code>
public class PhotoPickerPlusTutorialApp extends PhotoPickerPlusApp {

}
</code></pre>

This way the developer can use his own methods and code inside the Application class. 

If the developer decides to extend the Application class instead of PhotoPickerPlusApp he must copy the all the code below:

<pre><code>
public class PhotoPickerPlusTutorialApp extends Application {
    private static ImageLoader createImageLoader(Context context) {
		ImageLoader imageLoader = new ImageLoader(context, R.drawable.placeholder);
		imageLoader.setDefaultImageSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 75, context.getResources()
						.getDisplayMetrics()));
		return imageLoader;
    }

    private ImageLoader mImageLoader;

    @Override
    public void onCreate() {
	super.onCreate();
	mImageLoader = createImageLoader(this);
	TokenAuthenticationProvider.init(getApplicationContext(), TokenType.ACCESS_TOKEN);
	PreferenceUtil.init(getApplicationContext());
	PhotoPickerPreferenceUtil.init(getApplicationContext());
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
</code></pre>

PhotoPickerPlusTutorialApp can also be neglected by registering PhotoPickerPlusApp into the manifest instead of PhotoPickerPlusTutoiralApp if the developer doesn't have the need for extending the Application class.

##PhotoPickerPlusTutorialActivity.java 
This Activity class contains a Button and a GridView. When the button is clicked, PhotoPickerPlusIntentWrapper starts ChooseServiceActivity. PhotoPickerPlusIntentWrapper is a wrapper class that wraps the parameters needed for the intent.

<pre><code>
private class OnPhotoPickerClickListener implements OnClickListener {
	@Override
	public void onClick(View v) {
	    PhotoPickerPlusIntentWrapper wrapper = new PhotoPickerPlusIntentWrapper(PhotoPickerPlusTutorialActivity.this);
		wrapper.setMultiPicker(isMultiPicker);
	    wrapper.startActivityForResult(PhotoPickerPlusTutorialActivity.this, PhotoPickerPlusIntentWrapper.REQUEST_CODE);
	}
    }
</code></pre>

ChooseServiceActivity contains a list of services and device photos albums. You can authenticate using Facebook, Flickr, Instagram and Picasa, browse albums and photos, browse device photos as well as take a photo with the camera. 
After selecting photos, a result is returned to the activity that started the component where the selected photos are displayed in a grid.

<pre><code>
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (resultCode != Activity.RESULT_OK) {
	    return;
	}
	final PhotoActivityIntentWrapper wrapper = new PhotoActivityIntentWrapper(data);
	grid.setAdapter(new GridAdapter(PhotoPickerPlusTutorialActivity.this, wrapper.getMediaCollection()));
	Log.d(TAG, wrapper.toString());
    }
</code></pre>

PhotoActivityIntentWrapper encapsulates different information available for the selected image. Some of that additional info might be null depending of its availability. Different paths inside Media model can point to the same location if there are no additional sizes available.


    
      

    
      
