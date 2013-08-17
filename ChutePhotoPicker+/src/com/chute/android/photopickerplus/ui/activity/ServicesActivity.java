package com.chute.android.photopickerplus.ui.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.araneaapps.android.libs.logger.ALog;
import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.dao.MediaDAO;
import com.chute.android.photopickerplus.ui.fragment.AlbumsFragment;
import com.chute.android.photopickerplus.ui.fragment.AlbumsFragment.SelectAlbumListener;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment.AssetFragmentListener;
import com.chute.android.photopickerplus.ui.fragment.FragmentServices;
import com.chute.android.photopickerplus.ui.fragment.FragmentServices.ServiceClickedListener;
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
import com.chute.sdk.v2.model.AccountAlbumModel;
import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.AccountModel;
import com.chute.sdk.v2.model.enums.AccountType;
import com.chute.sdk.v2.model.response.ListResponseModel;
import com.chute.sdk.v2.utils.PreferenceUtil;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class ServicesActivity extends FragmentActivity implements SelectAlbumListener, AssetFragmentListener,
		ServiceClickedListener {

	private static final String TAG = ServicesActivity.class.getSimpleName();

	private AccountType accountType;
	private PhotoPickerPlusIntentWrapper ppWrapper;

	private FragmentServices fragmentServicesVertical;

	private boolean dualFragments = false;

	private FragmentTransaction fragmentTransaction;
	private static FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentManager = getSupportFragmentManager();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_layout);

		ppWrapper = new PhotoPickerPlusIntentWrapper(getIntent());
		fragmentServicesVertical = (FragmentServices) fragmentManager.findFragmentById(R.id.fragmentServices);
		dualFragments = getResources().getBoolean(R.bool.has_two_panes);

		ArrayList<AccountType> serviceList = new ArrayList<AccountType>();
		if (PreferenceUtil.get().hasAccountName(AccountType.FACEBOOK)) {
			serviceList.add(AccountType.FACEBOOK);
		}
		if (PreferenceUtil.get().hasAccountName(AccountType.FLICKR)) {
			serviceList.add(AccountType.FLICKR);
		}
		if (PreferenceUtil.get().hasAccountName(AccountType.PICASA)) {
			serviceList.add(AccountType.PICASA);
		}
		if (PreferenceUtil.get().hasAccountName(AccountType.INSTAGRAM)) {
			serviceList.add(AccountType.INSTAGRAM);
		}
		fragmentServicesVertical.configureServices(serviceList);

	}

	@Override
	public void takePhoto() {
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			NotificationUtil.makeToast(getApplicationContext(), R.string.toast_feature_camera);
			return;
		}
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (AppUtil.hasImageCaptureBug() == false) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(AppUtil.getTempFile(ServicesActivity.this)));
		} else {
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, Constants.CAMERA_PIC_REQUEST);

	}

	@Override
	public void photoStream() {
		final PhotosIntentWrapper wrapper = new PhotosIntentWrapper(ServicesActivity.this);
		wrapper.setFilterType(PhotoFilterType.ALL_PHOTOS);
		wrapper.setMultiPicker(ppWrapper.getIsMultiPicker());
		wrapper.setChuteId(ppWrapper.getAlbumId());
		wrapper.startActivityForResult(ServicesActivity.this, PhotosIntentWrapper.ACTIVITY_FOR_RESULT_STREAM_KEY);

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

			IntentUtil.deliverDataToInitialActivity(ServicesActivity.this, model, ppWrapper.getAlbumId());
		}

	}

	@Override
	public void cameraRoll() {
		final PhotosIntentWrapper wrapper = new PhotosIntentWrapper(ServicesActivity.this);
		wrapper.setMultiPicker(ppWrapper.getIsMultiPicker());
		wrapper.setFilterType(PhotoFilterType.CAMERA_ROLL);
		wrapper.setChuteId(ppWrapper.getAlbumId());
		wrapper.startActivityForResult(ServicesActivity.this, PhotosIntentWrapper.ACTIVITY_FOR_RESULT_STREAM_KEY);

	}

	@Override
	public void accountLogin(AccountType type) {
		accountType = type;
		if (PreferenceUtil.get().hasAccountId(accountType)) {
			accountClicked(PreferenceUtil.get().getAccountId(accountType), accountType.getName());
		} else {
			PhotoPickerPreferenceUtil.get().setAccountType(accountType.name());
			AuthenticationFactory.getInstance().startAuthenticationActivity(ServicesActivity.this, accountType);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
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

				IntentUtil.deliverDataToInitialActivity(this, model, ppWrapper.getAlbumId());
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setResult(Activity.RESULT_OK, new Intent().putExtras(intent.getExtras()));
		finish();
	}

	public void replaceContentWithAssetFragment(PhotoFilterType filterType, String accountID, String accountModelID,
			boolean isMultiPicker) {
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragments,
				AssetsFragment.newInstance(filterType, accountID, accountModelID, isMultiPicker));
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();

	}

	public void replaceContentWithAlbumFragment(String accountName, String accountID) {
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragments, AlbumsFragment.newInstance(accountName, accountID),
				Constants.TAG_FRAGMENT_ALBUM);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	// public void replaceContentWithEmptyFragment() {
	// fragmentTransaction = fragmentManager.beginTransaction();
	// fragmentTransaction.replace(R.id.fragments, EmptyFragment.newInstance(),
	// Constants.TAG_FRAGMENT_EMPTY);
	// // fragmentTransaction.addToBackStack(null);
	// fragmentTransaction.commit();
	// }

	private final class AccountsCallback implements HttpCallback<ListResponseModel<AccountModel>> {

		@Override
		public void onSuccess(ListResponseModel<AccountModel> responseData) {
			if (accountType == null) {
				// return;
				String type = PhotoPickerPreferenceUtil.get().getAccountType();
				accountType = AccountType.valueOf(type);
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
				// setAccountUserName();
				accountClicked(accountModel.getId(), accountType.getName());
			}
		}

		@Override
		public void onHttpError(ResponseStatus responseStatus) {
			ALog.d("Http Error: " + responseStatus.getStatusCode() + " " + responseStatus.getStatusMessage());
		}

	}

	public void accountClicked(String accountId, String accountName) {
		if (dualFragments) {
			replaceContentWithAlbumFragment(accountName, accountId);
		} else {
			AlbumsActivityIntentWrapper wrapper = new AlbumsActivityIntentWrapper(ServicesActivity.this);
			wrapper.setMultiPicker(ppWrapper.getIsMultiPicker());
			wrapper.setAccountId(accountId);
			wrapper.setAccountName(accountName);
			wrapper.startActivity(ServicesActivity.this);
		}
		// setAccountUserName();
	}

	@Override
	public void onConfirmedSocialAssets(ArrayList<AccountMediaModel> accountMediaModelList, String albumId) {
		IntentUtil.deliverDataToInitialActivity(ServicesActivity.this, accountMediaModelList, null, null, albumId);

	}

	@Override
	public void onConfirmedCursorAssets(ArrayList<String> assetPathList, String albumId) {
		IntentUtil.deliverDataToInitialActivity(ServicesActivity.this, AppUtil.getPhotoCollection(assetPathList), null,
				null, albumId);

	}

	@Override
	public void onSelectedSocialItem(AccountMediaModel accountMediaModel, String albumId) {
		IntentUtil.deliverDataToInitialActivity(ServicesActivity.this, accountMediaModel, albumId);
	}

	@Override
	public void onSelectedCursorItem(AccountMediaModel accountMediaModel, String albumId) {
		IntentUtil.deliverDataToInitialActivity(ServicesActivity.this, accountMediaModel, albumId);
	}

	@Override
	public void onAlbumSelected(AccountAlbumModel model, String accountId) {
		final PhotosIntentWrapper wrapper = new PhotosIntentWrapper(ServicesActivity.this);
		wrapper.setMultiPicker(ppWrapper.getIsMultiPicker());
		wrapper.setFilterType(PhotoFilterType.SOCIAL_PHOTOS);
		wrapper.setChuteId(ppWrapper.getAlbumId());
		wrapper.setAccountId(accountId);
		wrapper.setAlbumId(model.getId());
		wrapper.startActivityForResult(ServicesActivity.this, PhotosIntentWrapper.ACTIVITY_FOR_RESULT_STREAM_KEY);

	}

	@Override
	public void onBackPressed() {
		AlbumsFragment frag = (AlbumsFragment) fragmentManager.findFragmentByTag(Constants.TAG_FRAGMENT_ALBUM);
		if (frag != null && fragmentManager.getBackStackEntryCount() > 0) {
			fragmentManager.popBackStackImmediate(fragmentManager.getBackStackEntryAt(0).getName(),
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
			fragmentManager.beginTransaction().detach(frag).commit();
		} else {
			super.onBackPressed();
		}
	}
}
