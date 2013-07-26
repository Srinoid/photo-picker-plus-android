In order to define the way our application looks and offer a clean UI design, we can create styles that budle properties of a view. This way we only have to set common attributes once and change the appearance of the entire application from one central place.  
pp_styles.xml defnies the pp+ application theme and styles for different views. 


Styles
====
A style is a set of one or more formatting attributes that you can apply as a unit to single elements in your layout XML file(s). For example, you could set the styles of all of your TextViews to have the style textViewTitle. This style could have custom text font, background color, gravity and padding properties.


XML file for the style (saved in res/values/):  
    ```
        <!-- TextView Fragment Title -->  
        <style name="textViewTtitle" parent="android:Widget.TextView">  
        <item name="android:background">@color/background_title</item>  
        <item name="android:gravity">center</item>  
        <item name="android:padding">5dp</item>  
        <item name="android:textAppearance">?android:attr/textAppearanceMediumInverse</item>
        </style>  
    ```

XML file that applies the style to a TextView (saved in res/layout/):  
    ``
        <TextView
        android:id="@+id/textViewSelectAlbumTitle"  
        style="@style/textViewTtitle"  
        android:layout_width="match_parent"  
        android:layout_height="wrap_content"  
        android:text="@string/select_an_album" >  
        </TextView>
    ``
 
Inheritance
====
The parent attribute in the ``<style>`` element lets you specify a style from which your style should inherit properties. You can use this to inherit properties from an existing style and then define only the properties that you want to change or add. You can inherit from styles that you've created yourself or from styles that are built into the platform.  
For example, imageViewAlbumCover style inherits the properties from imageViewDefault style, defines its own width and height properties and overrides the background drawable property.  
  
   ```
             <!-- ImageView Default -->  
             <style name="imageViewDefault">  
                 <item name="android:background">@drawable/default_thumb</item>  
                 <item name="android:scaleType">centerCrop</item>  
             </style>  
    ```
    ```  
             <!-- ImageView Album Cover -->  
             <style name="imageViewAlbumCover" parent="imageViewDefault">  
                  <item name="android:background">@drawable/icon_camera</item>  
                  <item name="android:layout_width">48dp</item>  
                  <item name="android:layout_height">48dp</item>  
             </style>
    ```

Themes
====
A theme is a style applied to an entire Activity or Application, rather than an individual View. When a style is applied as a theme, every View in the Activity or Application will apply each style property that it supports.  
 For example, PhotoPickerTheme defines a custom color for the windowBackground property which will result in all Activities sharing the same background color applied in this style. 

   ```
           <!-- PhotoPicker+ Theme -->  
           <style name="PhotoPickerTheme" parent="android:Theme.Light.NoTitleBar">  
               <item name="android:windowBackground">@color/background_activity</item>  
           </style>
   ```

To define custom themes, place the theme XML files in the /res/values folder. Note that you can also define version-specific themes by adding a values-version folder. A theme defined in values-v11 will be used for API level 11  (3.0/Honeycomb) and above, and a theme defined in values-v14 will be used for API level 14 (4.0/Ice Cream Sandwich) and above.  For example, if you want your theme to be based on the Holo theme in Android 4.0 and above, you could create two themes files:

   ```
/res/values/styles.xml  
<!-- A simple theme based on the default theme for Android API level 10 and below -->  
<style name="PhotoPickerTheme" parent="android:Theme.Ligh.NoTitleBar">  
    <item name="android:windowBackground">@drawable/background_activity</item>  
</style>  
   ```
   ```  
/res/values-v14/styles.xml  
<!-- For API level 14 and above, use the Holo theme -->  
<style name="PhotoPickerTheme" parent="android:Holo">  
    <item name="android:windowBackground">@drawable/background_activity</item>  
</style>
   ```


Set the theme in Manifest
====
To set this theme for all the activites of your application, open the AndroidManifest.xml file and edit the ``<application>`` tag to include the android:theme attribute with the theme name:

``<application android:theme="@style/PhotoPickerTheme" >``  

If you want the theme applied to just one Activity in your application, then add the theme attribute to the ``<activity>`` tag, instead.



