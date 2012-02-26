package com.chute.android.photopickerplus.app;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.adapter.PhotoSelectCursorAdapter;
import com.chute.android.photopickerplus.dao.MediaDAO;
import com.chute.android.photopickerplus.util.NotificationUtil;
import com.chute.android.photopickerplus.util.intent.IntentUtil;
import com.chute.android.photopickerplus.util.intent.PhotoStreamActivityIntentWrapper;
import com.chute.sdk.collections.GCAccountMediaCollection;
import com.chute.sdk.model.GCAccountMediaModel;

public class PhotoStreamActivity extends Activity {

    public static final String TAG = PhotoStreamActivity.class.getSimpleName();
    private GridView grid;
    private PhotoSelectCursorAdapter gridAdapter;
    PhotoStreamActivityIntentWrapper photoStreamWrapper;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.photos_select);

	grid = (GridView) findViewById(R.id.gridView);
	grid.setEmptyView(findViewById(R.id.empty_view_layout));
	photoStreamWrapper = new PhotoStreamActivityIntentWrapper(getIntent());
	new LoadCursorTask().execute();

	Button ok = (Button) findViewById(R.id.btnOk);
	ok.setOnClickListener(new OkClickListener());
	Button cancel = (Button) findViewById(R.id.btnCancel);
	cancel.setOnClickListener(new CancelClickListener());

    }

    private class LoadCursorTask extends AsyncTask<Void, Void, Cursor> {

	@Override
	protected Cursor doInBackground(final Void... arg0) {
	    if (photoStreamWrapper.getFilterType() == PhotoStreamActivityIntentWrapper.TYPE_ALL_PHOTOS) {
		return MediaDAO.getAllMediaPhotos(getApplicationContext());
	    } else if (photoStreamWrapper.getFilterType() == PhotoStreamActivityIntentWrapper.TYPE_CAMERA_ROLL) {
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
	    if (gridAdapter == null) {
		gridAdapter = new PhotoSelectCursorAdapter(PhotoStreamActivity.this, result);
		grid.setAdapter(gridAdapter);
		grid.setOnItemClickListener(new OnGridItemClickListener());
	    } else {
		gridAdapter.changeCursor(result);
	    }
	    NotificationUtil
		    .showPhotosAdapterToast(getApplicationContext(), gridAdapter.getCount());
	}
    }

    private final class OnGridItemClickListener implements OnItemClickListener {

	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position,
		final long id) {
	    gridAdapter.toggleTick(position);
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
		    getPhotoCollection(gridAdapter.getSelectedFilePaths()), null, null);
	    setResult(RESULT_OK);
	    finish();
	}

    }

    // public GCAccountMediaModel getPhotoModel(String path) {
    // final GCAccountMediaModel model = new GCAccountMediaModel();
    // path = Uri.fromFile(new File(path)).toString();
    // model.setLargeUrl(path);
    // model.setThumbUrl(path);
    // model.setUrl(path);
    // return model;
    //
    // }

    public GCAccountMediaCollection getPhotoCollection(ArrayList<String> paths) {
	final GCAccountMediaCollection collection = new GCAccountMediaCollection();
	for (String path : paths) {
	    final GCAccountMediaModel model = new GCAccountMediaModel();
	    path = Uri.fromFile(new File(path)).toString();
	    model.setLargeUrl(path);
	    model.setThumbUrl(path);
	    model.setUrl(path);
	    collection.add(model);
	}
	return collection;
    }

}
