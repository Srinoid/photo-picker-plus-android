/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.adapter.PhotosAdapter;
import com.chute.android.photopickerplus.util.NotificationUtil;
import com.chute.android.photopickerplus.util.intent.IntentUtil;
import com.chute.android.photopickerplus.util.intent.PhotoActivityIntentWrapper;
import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.api.account.GCAccounts;
import com.chute.sdk.collections.GCAccountMediaCollection;
import com.chute.sdk.model.GCHttpRequestParameters;

public class PhotosActivity extends Activity {

	public static final String TAG = PhotosActivity.class.getSimpleName();

	private GridView grid;
	private PhotosAdapter adapter;

	private String accountId;
	private String albumId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photos_select);

		grid = (GridView) findViewById(R.id.gridView);

		Button ok = (Button) findViewById(R.id.btnOk);
		ok.setOnClickListener(new OkClickListener());
		Button cancel = (Button) findViewById(R.id.btnCancel);
		cancel.setOnClickListener(new CancelClickListener());

		PhotoActivityIntentWrapper wrapper = new PhotoActivityIntentWrapper(
				getIntent());
		accountId = wrapper.getAccountId();
		albumId = wrapper.getAlbumId();
		grid.setEmptyView(findViewById(R.id.empty_view_layout));
		GCAccounts.objectMedia(getApplicationContext(), accountId, albumId,
				new PhotoListCallback()).executeAsync();
	}

	private final class PhotoListCallback implements
			GCHttpCallback<GCAccountMediaCollection> {

		@Override
		public void onSuccess(GCAccountMediaCollection responseData) {
			adapter = new PhotosAdapter(PhotosActivity.this, responseData);
			grid.setAdapter(adapter);
			grid.setOnItemClickListener(new OnGridItemClickListener());
			NotificationUtil.showPhotosAdapterToast(getApplicationContext(),
					adapter.getCount());
		}

		@Override
		public void onHttpException(GCHttpRequestParameters params,
				Throwable exception) {
			NotificationUtil
					.makeConnectionProblemToast(getApplicationContext());
			toggleEmptyViewErrorMessage();
		}

		@Override
		public void onHttpError(int responseCode, String statusMessage) {
			NotificationUtil.makeServerErrorToast(getApplicationContext());
			toggleEmptyViewErrorMessage();
		}

		@Override
		public void onParserException(int responseCode, Throwable exception) {
			NotificationUtil.makeParserErrorToast(getApplicationContext());
			toggleEmptyViewErrorMessage();
		}

		public void toggleEmptyViewErrorMessage() {
			findViewById(R.id.empty_view_layout).setVisibility(View.GONE);
		}
	}

	private final class OnGridItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(final AdapterView<?> parent, final View view,
				final int position, final long id) {
			adapter.toggleTick(position);
		}
	}

	private final class OkClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			IntentUtil.deliverDataToInitialActivity(PhotosActivity.this,
					adapter.getPhotoCollection(), null, null);
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
}
