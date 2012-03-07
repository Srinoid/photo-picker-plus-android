/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.app;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.adapter.PhotoSelectCursorAdapter;
import com.chute.android.photopickerplus.dao.MediaDAO;
import com.chute.android.photopickerplus.util.AppUtil;
import com.chute.android.photopickerplus.util.NotificationUtil;
import com.chute.android.photopickerplus.util.intent.IntentUtil;
import com.chute.android.photopickerplus.util.intent.PhotoStreamActivityIntentWrapper;

public class PhotoStreamActivity extends Activity {

    public static final String TAG = PhotoStreamActivity.class.getSimpleName();
    private GridView grid;
    private PhotoSelectCursorAdapter adapter;
    PhotoStreamActivityIntentWrapper wrapper;
    private TextView selectPhotos;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.photos_select);

	selectPhotos = (TextView) findViewById(R.id.txt_select_photos);
	grid = (GridView) findViewById(R.id.gridView);
	grid.setEmptyView(findViewById(R.id.empty_view_layout));
	wrapper = new PhotoStreamActivityIntentWrapper(getIntent());
	new LoadCursorTask().execute();

	Button ok = (Button) findViewById(R.id.btnOk);
	ok.setOnClickListener(new OkClickListener());
	Button cancel = (Button) findViewById(R.id.btnCancel);
	cancel.setOnClickListener(new CancelClickListener());

    }

    private class LoadCursorTask extends AsyncTask<Void, Void, Cursor> {

	@Override
	protected Cursor doInBackground(final Void... arg0) {
	    if (wrapper.getFilterType() == PhotoStreamActivityIntentWrapper.TYPE_ALL_PHOTOS) {
		return MediaDAO.getAllMediaPhotos(getApplicationContext());
	    } else if (wrapper.getFilterType() == PhotoStreamActivityIntentWrapper.TYPE_CAMERA_ROLL) {
		return MediaDAO.getCameraPhotos(getApplicationContext());
	    } else {
		return null;
	    }
	}

	@Override
	protected void onPostExecute(final Cursor result) {
	    super.onPostExecute(result);
	    if (result == null) {
		return;
	    }
	    adapter = new PhotoSelectCursorAdapter(PhotoStreamActivity.this, result);
	    grid.setAdapter(adapter);
	    if (wrapper.getIsMultiPicker() == true) {
		selectPhotos.setText(getApplicationContext().getResources().getString(
			R.string.select_photos));
		grid.setOnItemClickListener(new OnMultiSelectGridItemClickListener());
	    } else {
		selectPhotos.setText(getApplicationContext().getResources().getString(
			R.string.select_a_photo));
		grid.setOnItemClickListener(new OnSingleSelectGridItemClickListener());
	    }
	    NotificationUtil.showPhotosAdapterToast(getApplicationContext(), adapter.getCount());
	}
    }

    private final class OnMultiSelectGridItemClickListener implements OnItemClickListener {

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position,
		final long id) {
	    adapter.toggleTick(position);
	}
    }

    private final class OnSingleSelectGridItemClickListener implements OnItemClickListener {

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position,
		final long id) {
	    IntentUtil.deliverDataToInitialActivity(PhotoStreamActivity.this,
		    AppUtil.getMediaModel(adapter.getItem(position)));
	    setResult(RESULT_OK);
	    finish();
	}
    }

    private final class CancelClickListener implements OnClickListener {

	@Override
	public void onClick(View v) {
	    finish();
	}

    }

    private final class OkClickListener implements OnClickListener {

	@Override
	public void onClick(View v) {
	    IntentUtil.deliverDataToInitialActivity(PhotoStreamActivity.this,
		    AppUtil.getPhotoCollection(adapter.getSelectedFilePaths()), null, null);
	    setResult(RESULT_OK);
	    finish();
	}

    }

}
