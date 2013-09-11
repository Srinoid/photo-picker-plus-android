/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.dao;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * The definition of the Database Access Objects that handles the reading and
 * writing a class from the database.
 * 
 */
public class MediaDAO {

  public static final String TAG = MediaDAO.class.getSimpleName();

  private MediaDAO() {
  }

  /**
   * Request a specific record in {@link MediaStore.Images.Media} database.
   * 
   * @param context
   *          The application context.
   * @return Cursor object enabling read-write access to camera photos on the
   *         device.
   */
  public static Cursor getCameraPhotos(final Context context) {
    final String[] projection = new String[] { MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATA };
    final Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    final String query = MediaStore.Images.Media.DATA + " LIKE \"%DCIM%\"";
    return context.getContentResolver().query(images, projection, query, null,
        MediaStore.Images.Media.DATE_ADDED + " DESC");
  }

  /**
   * Request a specific record in {@link MediaStore.Images.Media} database.
   * 
   * @param context
   *          The application context.
   * @return Cursor object enabling read-write access to all photos on
   *         the device.
   */
  public static Cursor getAllMediaPhotos(final Context context) {
    final String[] projection = new String[] { MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATA };
    final Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    return context.getContentResolver().query(images, projection, null, null,
        MediaStore.Images.Media.DATE_ADDED + " DESC");
  }

  /**
   * Returns the last photo URI from all photos on the device.
   * 
   * @param context
   *          The application context.
   * @return The URI for the requested query.
   */
  public static Uri getLastPhotoFromAllPhotos(final Context context) {
    Cursor allMediaPhotos = getAllMediaPhotos(context);
    Uri uri = getFirstItemUri(allMediaPhotos);
    safelyCloseCursor(allMediaPhotos);
    if (uri == null) {
      return Uri.parse("");
    }
    return uri;
  }

  /**
   * Returns the last photo URI from the camera photos on the device.
   * 
   * @param context
   *          The application context.
   * @return The URI for the requested query.
   */
  public static Uri getLastPhotoFromCameraPhotos(final Context context) {
    Cursor allMediaPhotos = getCameraPhotos(context);
    Uri uri = getFirstItemUri(allMediaPhotos);
    safelyCloseCursor(allMediaPhotos);
    if (uri == null) {
      return Uri.parse("");
    }
    return uri;
  }

  /**
   * Returns the URI of the first item from all photos on the device.
   * 
   * @param Cursor
   *          Cursor object enabling read-write access to all photos on the
   *          device.
   * @return The URI for the requested query.
   */
  private static Uri getFirstItemUri(Cursor allMediaPhotos) {
    if (allMediaPhotos != null && allMediaPhotos.moveToFirst()) {
      return Uri.fromFile(new File(allMediaPhotos.getString(allMediaPhotos
          .getColumnIndex(MediaStore.Images.Media.DATA))));
    }
    return null;
  }

  /**
   * Closes the Cursor, releasing all of its resources and making it completely
   * invalid.
   * 
   * @param c
   *          Cursor object enabling random read-write access to the result set
   *          returned by a database query.
   */
  public static void safelyCloseCursor(final Cursor c) {
    try {
      if (c != null) {
        c.close();
      }
    } catch (Exception e) {
      Log.d(TAG, "", e);
    }
  }
}
