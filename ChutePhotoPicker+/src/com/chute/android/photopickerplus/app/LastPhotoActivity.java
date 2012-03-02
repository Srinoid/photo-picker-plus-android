package com.chute.android.photopickerplus.app;

import java.io.File;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.dao.MediaDAO;
import com.darko.imagedownloader.ImageLoader;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class LastPhotoActivity extends Activity {

	public static final String TAG = LastPhotoActivity.class.getSimpleName();
	private ImageLoader loader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.last_photo);

		ImageView lastPhoto = (ImageView) findViewById(R.id.lastPhotoImg);

		loader = ImageLoader.getLoader(LastPhotoActivity.this);

		Cursor cursorLastPhoto = MediaDAO
				.getCameraPhotos(LastPhotoActivity.this);
		if (cursorLastPhoto != null && cursorLastPhoto.moveToLast()) {
			String path = cursorLastPhoto.getString(cursorLastPhoto
					.getColumnIndex(MediaStore.Images.Media.DATA));
			loader.displayImage(Uri.fromFile(new File(path)).toString(),
					lastPhoto);
		}

		lastPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

}
