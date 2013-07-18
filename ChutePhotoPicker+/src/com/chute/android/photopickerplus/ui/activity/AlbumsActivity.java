/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.ui.fragment.AlbumsFragment;
import com.chute.android.photopickerplus.ui.fragment.AlbumsFragment.SelectAlbumListener;
import com.chute.android.photopickerplus.util.PhotoFilterType;
import com.chute.android.photopickerplus.util.intent.AlbumsActivityIntentWrapper;
import com.chute.android.photopickerplus.util.intent.GridActivityIntentWrapper;
import com.chute.android.photopickerplus.util.intent.PhotosIntentWrapper;
import com.chute.sdk.v2.model.AccountObjectModel;

public class AlbumsActivity extends FragmentActivity implements SelectAlbumListener {

	@SuppressWarnings("unused")
	private static final String TAG = AlbumsActivity.class.getSimpleName();
	private String accountId;
	private boolean isMultiPicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_albums);

		AlbumsActivityIntentWrapper wrapper = new AlbumsActivityIntentWrapper(getIntent());
		String accountTitle = wrapper.getAccountName();
		accountId = wrapper.getAccountId();
		isMultiPicker = wrapper.getIsMultiPicker();

		AlbumsFragment frag = (AlbumsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentAlbums);
		frag.updateFragment(accountTitle, accountId);
	}

	@Override
	public void onAlbumSelected(AccountObjectModel model) {
		final PhotosIntentWrapper photosWrapper = new PhotosIntentWrapper(AlbumsActivity.this);
		photosWrapper.setFilterType(PhotoFilterType.SOCIAL_PHOTOS);
		photosWrapper.setMultiPicker(isMultiPicker);
		photosWrapper.setAlbumId(model.getId());
		photosWrapper.setAccountId(accountId);
		photosWrapper.startActivityForResult(AlbumsActivity.this,
				GridActivityIntentWrapper.ACTIVITY_FOR_RESULT_PHOTO_KEY);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			finish();
		}
	}
}
