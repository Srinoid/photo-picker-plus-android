/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.dao;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MediaDAO {

    public static final String TAG = MediaDAO.class.getSimpleName();

    private MediaDAO() {
    }

    public static Cursor getCameraPhotos(final Context context) {
        final String[] projection = new String[] {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        final Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        final String query = MediaStore.Images.Media.DATA + " LIKE \"%DCIM%\"";
        return context.getContentResolver().query(images, projection, query, null, null);
    }

    public static Cursor getAllMediaPhotos(final Context context) {
        final String[] projection = new String[] {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        final Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        return context.getContentResolver().query(images, projection, null, null, null);
    }
}
