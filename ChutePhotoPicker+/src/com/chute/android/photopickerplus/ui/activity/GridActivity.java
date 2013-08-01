/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.ui.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment.ButtonCancelListener;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment.ButtonConfirmCursorAssetsListener;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment.ButtonConfirmSocialAssetsListener;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment.GridCursorSingleSelectListener;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment.GridSocialSingleSelectListener;
import com.chute.android.photopickerplus.util.AppUtil;
import com.chute.android.photopickerplus.util.PhotoFilterType;
import com.chute.android.photopickerplus.util.intent.IntentUtil;
import com.chute.android.photopickerplus.util.intent.PhotosIntentWrapper;
import com.chute.sdk.v2.model.AccountMediaModel;

public class GridActivity extends FragmentActivity implements GridCursorSingleSelectListener,
		GridSocialSingleSelectListener, ButtonConfirmSocialAssetsListener, ButtonConfirmCursorAssetsListener{

	public static final String TAG = GridActivity.class.getSimpleName();
	private String albumID;
	private String accountID;
	private boolean isMultiPicker;
	private PhotoFilterType filterType;
	private PhotosIntentWrapper wrapper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_assets);

		wrapper = new PhotosIntentWrapper(getIntent());
		albumID = wrapper.getAlbumId();
		accountID = wrapper.getAccountId();
		isMultiPicker = wrapper.getIsMultiPicker();
		filterType = wrapper.getFilterType();

		AssetsFragment fragment = (AssetsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentAssets);
		fragment.updateFragment(albumID, accountID, filterType, isMultiPicker);

	}

	@Override
	public void onSelectedSocialItem(AccountMediaModel accountMediaModel, String albumId) {
		IntentUtil.deliverDataToInitialActivity(GridActivity.this, accountMediaModel, albumId);
		setResult(RESULT_OK);
		finish();

	}

	@Override
	public void onSelectedCursorItem(AccountMediaModel accountMediaModel, String albumId) {
		IntentUtil.deliverDataToInitialActivity(GridActivity.this, accountMediaModel, albumId);
		setResult(RESULT_OK);
		finish();

	}

	@Override
	public void onConfirmedCursorAssets(ArrayList<String> assetPathList, String albumId) {
		IntentUtil.deliverDataToInitialActivity(GridActivity.this, AppUtil.getPhotoCollection(assetPathList), null,
				null, albumId);
		setResult(RESULT_OK);
		finish();

	}

	@Override
	public void onConfirmedSocialAssets(ArrayList<AccountMediaModel> accountMediaModelList, String albumId) {
		IntentUtil.deliverDataToInitialActivity(GridActivity.this, accountMediaModelList, null, null, albumId);
		setResult(RESULT_OK);
		finish();

	}


}
