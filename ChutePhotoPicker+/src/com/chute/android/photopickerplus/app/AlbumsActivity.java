/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.adapter.AlbumsAdapter;
import com.chute.android.photopickerplus.api.GCAccounts;
import com.chute.android.photopickerplus.model.AccountObjectModel;
import com.chute.android.photopickerplus.util.AppUtil;
import com.chute.android.photopickerplus.util.NotificationUtil;
import com.chute.android.photopickerplus.util.intent.AlbumsActivityIntentWrapper;
import com.chute.android.photopickerplus.util.intent.PhotoActivityIntentWrapper;
import com.chute.android.photopickerplus.util.intent.PhotosIntentWrapper;
import com.chute.sdk.v2.model.response.ListResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class AlbumsActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = AlbumsActivity.class.getSimpleName();

	private ListView albums;
	private AlbumsAdapter adapter;
	private AlbumsActivityIntentWrapper wrapper;
	private View emptyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.albums_activity);

		albums = (ListView) findViewById(R.id.albumList);
		emptyView = findViewById(R.id.empty_view_layout);
		albums.setEmptyView(emptyView);
		wrapper = new AlbumsActivityIntentWrapper(getIntent());

		TextView title = (TextView) findViewById(R.id.title);
		String albumName = AppUtil.asUpperCaseFirstChar(wrapper.getAccountName().concat(" Albums"));
		title.setText(albumName);

		GCAccounts.objects(getApplicationContext(), wrapper.getAccountId(), new ObjectsCallback()).executeAsync();

	}

	private final class ObjectsCallback implements HttpCallback<ListResponseModel<AccountObjectModel>> {

		@Override
		public void onSuccess(ListResponseModel<AccountObjectModel> responseData) {
			adapter = new AlbumsAdapter(AlbumsActivity.this, (ArrayList<AccountObjectModel>) responseData.getData());
			albums.setAdapter(adapter);
			if (adapter.getCount() == 0) {
				emptyView.setVisibility(View.GONE);
			}
			albums.setOnItemClickListener(new OnAlbumsClickListener());
			NotificationUtil.showAlbumsAdapterToast(getApplicationContext(), adapter.getCount());
		}

		public void toggleEmptyViewErrorMessage() {
			findViewById(R.id.empty_view_layout).setVisibility(View.GONE);
		}

		@Override
		public void onHttpError(ResponseStatus responseStatus) {
			NotificationUtil.makeConnectionProblemToast(getApplicationContext());
			toggleEmptyViewErrorMessage();

		}
	}

	private final class OnAlbumsClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			final String albumId = adapter.getItem(position).getId();
			final PhotosIntentWrapper photosWrapper = new PhotosIntentWrapper(AlbumsActivity.this);
			photosWrapper.setFilterType(PhotosIntentWrapper.TYPE_SOCIAL_PHOTOS);
			photosWrapper.setMultiPicker(wrapper.getIsMultiPicker());
			photosWrapper.setAlbumId(albumId);
			photosWrapper.setAccountId(wrapper.getAccountId());
			photosWrapper.startActivityForResult(AlbumsActivity.this,
					PhotoActivityIntentWrapper.ACTIVITY_FOR_RESULT_PHOTO_KEY);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			finish();
		}
	}

}
