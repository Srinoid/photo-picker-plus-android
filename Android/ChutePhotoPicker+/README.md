Introduction
====

PhotoPicker+ is a component made for developers to easily integrate it inside their application and enable photo picking functionality. This component enables browsing photos from services such as Facebook, Picasa, Instagram, Flickr, Google+, Google Drive, SkyDrive and Dropbox. It also enables taking photos with the native camera application and choosing photos from the device.


Screenshots
====

![nexus_screenshot1](/screenshots/nexus_screenshot1.png)![nexus_screenshot2](/screenshots/nexus_screenshot2.png)![nexus_screenshot3](/screenshots/nexus_screenshot3.png)![nexus_screenshot4](/screenshots/nexus_screenshot4.png)![nexus_screenshot5](/screenshots/nexus_screenshot5.png)

Setup
====

PhotoPicker+ uses [https://github.com/chute/Chute-SDK-V2-Android](https://github.com/chute/Chute-SDK-V2-Android) library. In order to add the library, create the directory libs/ at the root of PhotoPicker+ project and add the jar file that is located in the target/ directory in Chute-SDK-V2-Android project.


Implementation
====

Follow the [PhotoPicker+ tutorial](../PhotoPickerPlusTutorial) on how to setup and use the component.


Localization
====

PhotoPicker+ provides alternative text for different languages. 
To add support for more languages, create additional values directories inside res/ that include a hyphen and the ISO country code at the end of the directory name. For example, values-fr/ is the directory containing simple resourcess for the Locales with the language code "fr". Android loads the appropriate resources according to the locale settings of the device at run time.
Once you’ve decided on the languages you will support, create the resource subdirectories and string resource files. For example:  
res/values/strings.xml  
Contains English text for all the strings that the application uses, including text for a string named title.  
res/values-fr/strings.xml  
Contain French text for all the strings, including title.  
res/values-ja/strings.xml  
Contain Japanese text for all the strings except title.

Some of PhotoPicker+ supported languages might not be correctly translated. Any feedback is welcomed.


Configuration
====
Follow [PhotoPickerConfiguration.md](PhotoPickerConfiguration.md) on how to initialize PhotoPicker+ component with configuration.


Styles setup
====

In order to achieve the desired look for your application, you need to override the existing styles or modify the defined resources. [PhotoPickerStyleGuide.md](PhotoPickerStyleGuide.md) will show you how to change views appearance or application theme and guide you throughout the way.


Modifying the component
====

Since the component is opensource you can easily modify the resources to your own needs. Just replace the drawables and modify the color values. Additionally you can even adjust the layouts.





