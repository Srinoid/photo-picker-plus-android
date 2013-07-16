/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.ui.activity;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.dao.MediaDAO;
import com.chute.android.photopickerplus.util.AppUtil;
import com.chute.android.photopickerplus.util.Constants;
import com.chute.android.photopickerplus.util.NotificationUtil;
import com.chute.android.photopickerplus.util.TokenAuthentication;
import com.chute.android.photopickerplus.util.intent.AlbumsActivityIntentWrapper;
import com.chute.android.photopickerplus.util.intent.IntentUtil;
import com.chute.android.photopickerplus.util.intent.PhotoPickerPlusIntentWrapper;
import com.chute.android.photopickerplus.util.intent.PhotosIntentWrapper;
import com.chute.sdk.v2.api.accounts.GCAccounts;
import com.chute.sdk.v2.api.authentication.AuthenticationFactory.AccountType;
import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.AccountModel;
import com.chute.sdk.v2.model.AccountStore;
import com.chute.sdk.v2.model.response.ListResponseModel;
import com.chute.sdk.v2.utils.PreferenceUtil;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

import darko.imagedownloader.ImageLoader;

public class ChooseServiceActivity extends Activity {

	public static final String TAG = ChooseServiceActivity.class.getSimpleName();

	private TextView textViewFacebook;
	private TextView textViewPicasa;
	private TextView textViewFlickr;
	private TextView textViewInstagram;
	private TextView textViewUserTitle;
	private LinearLayout linearLayoutServices;
	private LinearLayout linearLayoutTakePhoto;
	private LinearLayout linearLayoutFacebook;
	private LinearLayout linearLayoutPicasa;
	private LinearLayout linearLayoutInstagram;
	private LinearLayout linearLayoutFlickr;
	private LinearLayout linearLayoutAllPhotos;
	private LinearLayout linearLayoutCameraPhotos;
	private LinearLayout linearLayoutLastPhoto;
	private ImageView imageViewAllPhotos;
	private ImageView imageViewCameraPhotos;
	private ImageView imageViewLastPhoto;
	private AccountType accountType;
	private ImageLoader loader;
	private PhotoPickerPlusIntentWrapper ppWrapper;

	private String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_services);

		loader = ImageLoader.getLoader(ChooseServiceActivity.this);

		ppWrapper = new PhotoPickerPlusIntentWrapper(getIntent());

		initViews();
		PhotoPickerPlusIntentWrapper photoPickerPlusIntentWrapper = new PhotoPickerPlusIntentWrapper(getIntent());
		if (photoPickerPlusIntentWrapper.areServicesHidden()) {
			linearLayoutServices.setVisibility(View.GONE);
			textViewUserTitle.setVisibility(View.GONE);
		}

		linearLayoutFacebook.setOnClickListener(new OnLoginClickListener());
		linearLayoutPicasa.setOnClickListener(new OnLoginClickListener());
		linearLayoutFlickr.setOnClickListener(new OnLoginClickListener());
		linearLayoutInstagram.setOnClickListener(new OnLoginClickListener());
		linearLayoutAllPhotos.setOnClickListener(new OnPhotoStreamListener());
		linearLayoutCameraPhotos.setOnClickListener(new OnCameraRollListener());
		linearLayoutLastPhoto.setOnClickListener(new OnLastPhotoClickListener());
		linearLayoutTakePhoto.setOnClickListener(new OnCameraClickListener());

		loader.displayImage(MediaDAO.getLastPhotoFromAllPhotos(getApplicationContext()).toString(), imageViewAllPhotos,
				null);

		Uri uri = MediaDAO.getLastPhotoFromCameraPhotos(getApplicationContext());
		if (uri != null) {
			loader.displayImage(uri.toString(), imageViewCameraPhotos, null);
			loader.displayImage(uri.toString(), imageViewLastPhoto, null);
		}

	}

	public void initViews() {
		textViewFacebook = (TextView) findViewById(R.id.textViewFacebook);
		textViewFacebook.setTag(AccountType.FACEBOOK);
		textViewPicasa = (TextView) findViewById(R.id.textViewPicasa);
		textViewPicasa.setTag(AccountType.PICASA);
		textViewFlickr = (TextView) findViewById(R.id.textViewFlickr);
		textViewFlickr.setTag(AccountType.FLICKR);
		textViewInstagram = (TextView) findViewById(R.id.textViewInstagram);
		textViewInstagram.setTag(AccountType.INSTAGRAM);
		textViewUserTitle = (TextView) findViewById(R.id.textViewUserTitle);

		linearLayoutServices = (LinearLayout) findViewById(R.id.linearLayoutSocialServicesContent);
		linearLayoutAllPhotos = (LinearLayout) findViewById(R.id.linearLayoutAllPhotos);
		linearLayoutCameraPhotos = (LinearLayout) findViewById(R.id.linearLayoutCameraShots);
		linearLayoutLastPhoto = (LinearLayout) findViewById(R.id.linearLayoutLastPhotoTaken);
		linearLayoutTakePhoto = (LinearLayout) findViewById(R.id.linearLayoutTakePhoto);

		linearLayoutFacebook = (LinearLayout) findViewById(R.id.linearLayoutFacebook);
		linearLayoutFacebook.setTag(AccountType.FACEBOOK);
		linearLayoutFlickr = (LinearLayout) findViewById(R.id.linearLayoutFlickr);
		linearLayoutFlickr.setTag(AccountType.FLICKR);
		linearLayoutPicasa = (LinearLayout) findViewById(R.id.linearLayoutPicasa);
		linearLayoutPicasa.setTag(AccountType.PICASA);
		linearLayoutInstagram = (LinearLayout) findViewById(R.id.linearLayoutInstagram);
		linearLayoutInstagram.setTag(AccountType.INSTAGRAM);

		imageViewAllPhotos = (ImageView) findViewById(R.id.imageViewAllPhotos);
		imageViewCameraPhotos = (ImageView) findViewById(R.id.imageViewCameraShots);
		imageViewLastPhoto = (ImageView) findViewById(R.id.imageViewLastPhotoTaken);

	}

	private final class OnLoginClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			accountType = (AccountType) v.getTag();
			if (PreferenceUtil.get().hasAccountId(accountType)) {
				accountClicked(PreferenceUtil.get().getAccountId(accountType), accountType.getName());
			} else {
				AccountStore.getInstance(getApplicationContext()).startAuthenticationActivity(
						ChooseServiceActivity.this, accountType, Constants.PERMISSIONS_SCOPE, Constants.CALLBACK_URL,
						Constants.APP_ID, Constants.APP_SECRET);
			}
		}
	}

	private final class AccountsCallback implements HttpCallback<ListResponseModel<AccountModel>> {

		@Override
		public void onSuccess(ListResponseModel<AccountModel> responseData) {
			if (accountType == null) {
				return;
			}
			if (responseData.getData().size() == 0) {
				Toast.makeText(getApplicationContext(), "No albums found", Toast.LENGTH_SHORT).show();
				return;
			}
			AccountModel accountModel = responseData.getData().get(0);
			if (accountModel.getType().equalsIgnoreCase(accountType.getName())) {
				PreferenceUtil.get().setNameForAccount(accountType, accountModel.getName());
				PreferenceUtil.get().setIdForAccount(accountType, accountModel.getId());
				accountClicked(accountModel.getId(), accountType.getName());
			}
		}

		@Override
		public void onHttpError(ResponseStatus responseStatus) {

		}

	}

	public void accountClicked(String accountId, String accountName) {
		AlbumsActivityIntentWrapper wrapper = new AlbumsActivityIntentWrapper(ChooseServiceActivity.this);
		wrapper.setMultiPicker(ppWrapper.getIsMultiPicker());
		wrapper.setAccountId(accountId);
		wrapper.setAccountName(accountName);
		wrapper.startActivity(ChooseServiceActivity.this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == AccountStore.AUTHENTICATION_REQUEST_CODE) {
				token = AccountStore.getInstance(getApplicationContext()).getPassword();
				PreferenceUtil.get().setAccountToken(token);
				TokenAuthentication.authenticate(getApplicationContext(), token);
				Log.d("debug", "token = " + token);
				GCAccounts.all(getApplicationContext(), new AccountsCallback()).executeAsync();
			}
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

	private class OnCameraClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
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
	}

	private final class OnPhotoStreamListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			final PhotosIntentWrapper wrapper = new PhotosIntentWrapper(ChooseServiceActivity.this);
			wrapper.setFilterType(PhotosIntentWrapper.TYPE_ALL_PHOTOS);
			wrapper.setMultiPicker(ppWrapper.getIsMultiPicker());
			wrapper.setChuteId(ppWrapper.getChuteId());
			wrapper.startActivityForResult(ChooseServiceActivity.this,
					PhotosIntentWrapper.ACTIVITY_FOR_RESULT_STREAM_KEY);
		}
	}

	private final class OnCameraRollListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			final PhotosIntentWrapper wrapper = new PhotosIntentWrapper(ChooseServiceActivity.this);
			wrapper.setMultiPicker(ppWrapper.getIsMultiPicker());
			wrapper.setFilterType(PhotosIntentWrapper.TYPE_CAMERA_ROLL);
			wrapper.setChuteId(ppWrapper.getChuteId());
			wrapper.startActivityForResult(ChooseServiceActivity.this,
					PhotosIntentWrapper.ACTIVITY_FOR_RESULT_STREAM_KEY);
		}

	}

	private final class OnLastPhotoClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Uri uri = MediaDAO.getLastPhotoFromCameraPhotos(getApplicationContext());
			if (uri.toString().equals("")) {
				NotificationUtil
						.makeToast(getApplicationContext(), getResources().getString(R.string.no_camera_photos));
			} else {
				final AccountMediaModel model = new AccountMediaModel();
				model.setLargeUrl(uri.toString());
				model.setThumbUrl(uri.toString());
				model.setUrl(uri.toString());

				IntentUtil.deliverDataToInitialActivity(ChooseServiceActivity.this, model, ppWrapper.getChuteId());
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
				textViewPicasa.setText(PreferenceUtil.get().getAccountName(AccountType.PICASA));

			}
		}
		if (PreferenceUtil.get().hasAccountId(AccountType.FACEBOOK)) {
			if (PreferenceUtil.get().hasAccountName(AccountType.FACEBOOK)) {
				textViewFacebook.setText(PreferenceUtil.get().getAccountName(AccountType.FACEBOOK));
			}
		}
		if (PreferenceUtil.get().hasAccountId(AccountType.FLICKR)) {
			if (PreferenceUtil.get().hasAccountName(AccountType.FLICKR)) {
				textViewFlickr.setText(PreferenceUtil.get().getAccountName(AccountType.FLICKR));
			}
		}
		if (PreferenceUtil.get().hasAccountId(AccountType.INSTAGRAM)) {
			if (PreferenceUtil.get().hasAccountName(AccountType.INSTAGRAM)) {
				textViewInstagram.setText(PreferenceUtil.get().getAccountName(AccountType.INSTAGRAM));
			}
		}
	}
}
