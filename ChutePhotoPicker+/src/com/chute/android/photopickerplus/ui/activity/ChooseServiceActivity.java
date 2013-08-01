/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.ui.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.dao.MediaDAO;
import com.chute.android.photopickerplus.ui.fragment.AlbumsFragment;
import com.chute.android.photopickerplus.ui.fragment.AlbumsFragment.SelectAlbumListener;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment.ButtonConfirmCursorAssetsListener;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment.ButtonConfirmSocialAssetsListener;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment.GridCursorSingleSelectListener;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment.GridSocialSingleSelectListener;
import com.chute.android.photopickerplus.ui.fragment.ChooseServiceFragment;
import com.chute.android.photopickerplus.ui.fragment.ChooseServiceFragment.CameraRollListener;
import com.chute.android.photopickerplus.ui.fragment.ChooseServiceFragment.LastPhotoListener;
import com.chute.android.photopickerplus.ui.fragment.ChooseServiceFragment.LoginListener;
import com.chute.android.photopickerplus.ui.fragment.ChooseServiceFragment.PhotoStreamListener;
import com.chute.android.photopickerplus.ui.fragment.ChooseServiceFragment.TakePhotoListener;
import com.chute.android.photopickerplus.util.AppUtil;
import com.chute.android.photopickerplus.util.Constants;
import com.chute.android.photopickerplus.util.NotificationUtil;
import com.chute.android.photopickerplus.util.PhotoFilterType;
import com.chute.android.photopickerplus.util.PhotoPickerPreferenceUtil;
import com.chute.android.photopickerplus.util.intent.AlbumsActivityIntentWrapper;
import com.chute.android.photopickerplus.util.intent.IntentUtil;
import com.chute.android.photopickerplus.util.intent.PhotoPickerPlusIntentWrapper;
import com.chute.android.photopickerplus.util.intent.PhotosIntentWrapper;
import com.chute.sdk.v2.api.accounts.GCAccounts;
import com.chute.sdk.v2.api.authentication.AuthenticationFactory;
import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.AccountModel;
import com.chute.sdk.v2.model.AccountObjectModel;
import com.chute.sdk.v2.model.AccountStore;
import com.chute.sdk.v2.model.enums.AccountType;
import com.chute.sdk.v2.model.response.ListResponseModel;
import com.chute.sdk.v2.utils.PreferenceUtil;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class ChooseServiceActivity extends FragmentActivity implements LoginListener, CameraRollListener,
		LastPhotoListener, PhotoStreamListener, TakePhotoListener, SelectAlbumListener, GridCursorSingleSelectListener,
		GridSocialSingleSelectListener, ButtonConfirmCursorAssetsListener, ButtonConfirmSocialAssetsListener {

	public static final String TAG = ChooseServiceActivity.class.getSimpleName();

	private AccountType accountType;
	private PhotoPickerPlusIntentWrapper ppWrapper;

	private ChooseServiceFragment chooseServiceFragment;

	private String token;

	private boolean dualFragments = false;

	private FragmentTransaction fragmentTransaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_layout);

		ppWrapper = new PhotoPickerPlusIntentWrapper(getIntent());
		chooseServiceFragment = (ChooseServiceFragment) getSupportFragmentManager().findFragmentById(
				R.id.fragmentChooseService);
		dualFragments = getResources().getBoolean(R.bool.has_two_panes);

		if (ppWrapper.areServicesHidden()) {
			chooseServiceFragment.setSocialServicesHidden();
		}

		String serialized = PhotoPickerPreferenceUtil.get().getServiceList();
		if (serialized == null) {
			Toast.makeText(getApplicationContext(), getString(R.string.connect_to_internet_to_cofigure_services),
					Toast.LENGTH_LONG).show();
		} else {
			List<String> serviceList = Arrays.asList(TextUtils.split(serialized, ","));
			chooseServiceFragment.configureServices(serviceList);
		}

	}

	private final class AccountsCallback implements HttpCallback<ListResponseModel<AccountModel>> {

		@Override
		public void onSuccess(ListResponseModel<AccountModel> responseData) {
			if (accountType == null) {
				return;
			}
			if (responseData.getData().size() == 0) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_albums_found),
						Toast.LENGTH_SHORT).show();
				return;
			}
			AccountModel accountModel = responseData.getData().get(0);
			if (accountModel.getType().equalsIgnoreCase(accountType.getName())) {
				if (accountModel.getName().equals("")) {
					PreferenceUtil.get().setNameForAccount(accountType, accountModel.getUsername());
				} else {
					PreferenceUtil.get().setNameForAccount(accountType, accountModel.getName());
				}
				PreferenceUtil.get().setIdForAccount(accountType, accountModel.getId());
				accountClicked(accountModel.getId(), accountType.getName());
			}
		}

		@Override
		public void onHttpError(ResponseStatus responseStatus) {
			Log.d("debug", "error = " + responseStatus.getStatusCode() + " " + responseStatus.getStatusMessage());
		}

	}

	public void accountClicked(String accountId, String accountName) {
		if (dualFragments) {
			replaceContentWithAlbumFragment(accountName, accountId);
		} else {
			AlbumsActivityIntentWrapper wrapper = new AlbumsActivityIntentWrapper(ChooseServiceActivity.this);
			wrapper.setMultiPicker(ppWrapper.getIsMultiPicker());
			wrapper.setAccountId(accountId);
			wrapper.setAccountName(accountName);
			wrapper.startActivity(ChooseServiceActivity.this);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			token = AccountStore.getInstance(getApplicationContext()).getPassword();
			PreferenceUtil.get().setAccountToken(token);
			GCAccounts.allUserAccounts(getApplicationContext(), new AccountsCallback()).executeAsync();
			if (requestCode == PhotosIntentWrapper.ACTIVITY_FOR_RESULT_STREAM_KEY) {
				finish();
			} else if (requestCode == Constants.CAMERA_PIC_REQUEST) {
				// Bitmap image = (Bitmap) data.getExtras().get("data");

				String path = "";
				File tempFile = AppUtil.getTempFile(getApplicationContext());
				if (AppUtil.hasImageCaptureBug() == false && tempFile.length() > 0) {
					try {
						android.provider.MediaStore.Images.Media.insertImage(getContentResolver(),
								tempFile.getAbsolutePath(), null, null);
						tempFile.delete();
						path = MediaDAO.getLastPhotoFromCameraPhotos(getApplicationContext()).toString();
					} catch (FileNotFoundException e) {
						Log.d(TAG, "", e);
					}
				} else {
					Log.e(TAG, "Bug " + data.getData().getPath());
					path = Uri.fromFile(new File(AppUtil.getPath(getApplicationContext(), data.getData()))).toString();
				}
				Log.d(TAG, path);
				final AccountMediaModel model = new AccountMediaModel();
				model.setLargeUrl(path);
				model.setThumbUrl(path);
				model.setUrl(path);

				IntentUtil.deliverDataToInitialActivity(this, model, ppWrapper.getChuteId());
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setResult(Activity.RESULT_OK, new Intent().putExtras(intent.getExtras()));
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (PreferenceUtil.get().hasAccountId(AccountType.PICASA)) {
			if (PreferenceUtil.get().hasAccountName(AccountType.PICASA)) {
				chooseServiceFragment.setUserName(AccountType.PICASA,
						PreferenceUtil.get().getAccountName(AccountType.PICASA));
			}
		}
		if (PreferenceUtil.get().hasAccountId(AccountType.FACEBOOK)) {
			if (PreferenceUtil.get().hasAccountName(AccountType.FACEBOOK)) {
				chooseServiceFragment.setUserName(AccountType.FACEBOOK,
						PreferenceUtil.get().getAccountName(AccountType.FACEBOOK));
			}
		}
		if (PreferenceUtil.get().hasAccountId(AccountType.FLICKR)) {
			if (PreferenceUtil.get().hasAccountName(AccountType.FLICKR)) {
				chooseServiceFragment.setUserName(AccountType.FLICKR,
						PreferenceUtil.get().getAccountName(AccountType.FLICKR));
			}
		}
		if (PreferenceUtil.get().hasAccountId(AccountType.INSTAGRAM)) {
			if (PreferenceUtil.get().hasAccountName(AccountType.INSTAGRAM)) {
				chooseServiceFragment.setUserName(AccountType.INSTAGRAM,
						PreferenceUtil.get().getAccountName(AccountType.INSTAGRAM));
			}
		}
	}

	@Override
	public void takePhoto() {
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			NotificationUtil.makeToast(getApplicationContext(), R.string.toast_feature_camera);
			return;
		}
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (AppUtil.hasImageCaptureBug() == false) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(AppUtil.getTempFile(ChooseServiceActivity.this)));
		} else {
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, Constants.CAMERA_PIC_REQUEST);

	}

	@Override
	public void photoStream() {
		if (dualFragments) {
			replaceContentWithAssetFragment(PhotoFilterType.ALL_PHOTOS, null, ppWrapper.getChuteId(),
					ppWrapper.getIsMultiPicker());
		} else {
			final PhotosIntentWrapper wrapper = new PhotosIntentWrapper(ChooseServiceActivity.this);
			wrapper.setFilterType(PhotoFilterType.ALL_PHOTOS);
			wrapper.setMultiPicker(ppWrapper.getIsMultiPicker());
			wrapper.setChuteId(ppWrapper.getChuteId());
			wrapper.startActivityForResult(ChooseServiceActivity.this,
					PhotosIntentWrapper.ACTIVITY_FOR_RESULT_STREAM_KEY);
		}

	}

	@Override
	public void lastPhoto() {
		Uri uri = MediaDAO.getLastPhotoFromCameraPhotos(getApplicationContext());
		if (uri.toString().equals("")) {
			NotificationUtil.makeToast(getApplicationContext(), getResources().getString(R.string.no_camera_photos));
		} else {
			final AccountMediaModel model = new AccountMediaModel();
			model.setLargeUrl(uri.toString());
			model.setThumbUrl(uri.toString());
			model.setUrl(uri.toString());

			IntentUtil.deliverDataToInitialActivity(ChooseServiceActivity.this, model, ppWrapper.getChuteId());
		}

	}

	@Override
	public void cameraRoll() {
		if (dualFragments) {
			replaceContentWithAssetFragment(PhotoFilterType.CAMERA_ROLL, null, ppWrapper.getChuteId(),
					ppWrapper.getIsMultiPicker());
		} else {
			final PhotosIntentWrapper wrapper = new PhotosIntentWrapper(ChooseServiceActivity.this);
			wrapper.setMultiPicker(ppWrapper.getIsMultiPicker());
			wrapper.setFilterType(PhotoFilterType.CAMERA_ROLL);
			wrapper.setChuteId(ppWrapper.getChuteId());
			wrapper.startActivityForResult(ChooseServiceActivity.this,
					PhotosIntentWrapper.ACTIVITY_FOR_RESULT_STREAM_KEY);
		}

	}

	@Override
	public void accountLogin(AccountType type) {
		accountType = type;
		if (PreferenceUtil.get().hasAccountId(accountType)) {
			accountClicked(PreferenceUtil.get().getAccountId(accountType), accountType.getName());
		} else {
			AuthenticationFactory.getInstance().startAuthenticationActivity(ChooseServiceActivity.this, accountType);
		}

	}

	@Override
	public void onAlbumSelected(AccountObjectModel model, String accountId) {
		replaceContentWithAssetFragment(PhotoFilterType.SOCIAL_PHOTOS, accountId, model.getId(),
				ppWrapper.getIsMultiPicker());

	}

	@Override
	public void onConfirmedSocialAssets(ArrayList<AccountMediaModel> accountMediaModelList, String albumId) {
		IntentUtil.deliverDataToInitialActivity(ChooseServiceActivity.this, accountMediaModelList, null, null, albumId);

	}

	@Override
	public void onConfirmedCursorAssets(ArrayList<String> assetPathList, String albumId) {
		IntentUtil.deliverDataToInitialActivity(ChooseServiceActivity.this, AppUtil.getPhotoCollection(assetPathList),
				null, null, albumId);

	}

	@Override
	public void onSelectedSocialItem(AccountMediaModel accountMediaModel, String albumId) {
		IntentUtil.deliverDataToInitialActivity(ChooseServiceActivity.this, accountMediaModel, albumId);
	}

	@Override
	public void onSelectedCursorItem(AccountMediaModel accountMediaModel, String albumId) {
		IntentUtil.deliverDataToInitialActivity(ChooseServiceActivity.this, accountMediaModel, albumId);
	}

	public void replaceContentWithAssetFragment(PhotoFilterType filterType, String accountID, String accountModelID,
			boolean isMultiPicker) {
		fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.fragments,
				AssetsFragment.newInstance(filterType, accountID, accountModelID, isMultiPicker));
		fragmentTransaction.commit();

	}

	public void replaceContentWithAlbumFragment(String accountName, String accountID) {
		fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.fragments, AlbumsFragment.newInstance(accountName, accountID), "AlbumFrag");
		fragmentTransaction.commit();
	}

}
