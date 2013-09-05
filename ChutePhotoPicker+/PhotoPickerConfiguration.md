In order to use the PhotoPicker+ component, you need to initialize it with configuration. 

Configuration builder
====
 
The Configuration builder allows you to choose which services (local and remote) your application is going to use. Local services included are: Camera shots, All photos, Last photo taken and Take photo. Remote supported services include: Google+, Google Drive, SkyDrive, Facebook, Instagram, Flickr, Picasa and Dropbox.  
All options in Configuration builder are required for appropriately setting up the services, except <code>isMultiPicker(false)</code>, which is optional, with false as a default value and <code>configUrl(url)</code>. If you want to use multipicking feature you should set <code>isMultiPicker(true)</code> option in the Configuration builder.  
<code>configUrl(url)</code> option must be initialized in the Configuration builder if you want to get the service list from a server. 
When started for the first time the app displays the initialized local and remote services, while on the second run it shows the complete list of services retrieved from the server if <code>configUrl(url)</code> option is initialized.  

<pre><code>
    PhotoPickerConfiguration config = new PhotoPickerConfiguration.Builder(
        getApplicationContext())
        .isMultiPicker(true)
        .accountList(AccountType.FACEBOOK, AccountType.INSTAGRAM)
        .localMediaList(LocalMediaType.ALL_PHOTOS, LocalMediaType.TAKE_PHOTO)
        .configUrl(ConfigEndpointURLs.SERVICES_CONFIG_URL)
        .build();
    PhotoPicker.getInstance().init(config);
</code></pre>
    

Default configuration
====

If the local and remote services are not initialized, the PhotoPicker will be initialized with default settings. DefaultConfigurationFactory sets <code>LocalMediaType.ALL_PHOTOS</code> and <code>LocalMediaType.CAMERA_PHOTOS</code> as local and <code>AccountType.FACEBOOK</code> and <code>AccountType.INSTAGRAM</code> as remote services.


Configuration URL
====

If you wish to configure services by getting a list from the server, you should create a file containing JSON model as the following one:
<code><pre>
 {"services":["facebook","instagram","skydrive","googledrive","google","picasa","flickr","dropbox"],"local_features":["all_photos","take_photo","last_taken_photo","camera_photos"]}
</pre></code>

**NOTE:** Supported remote services include: Facebook, Instagram, Flickr, Picasa, Google, GoogleDrive, SkyDrive, Dropbox.  
Supported local services include: All photos, Camera photos, Last photo taken, Take photo.
