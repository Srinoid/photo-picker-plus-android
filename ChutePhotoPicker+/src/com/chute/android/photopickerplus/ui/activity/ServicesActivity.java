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
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.araneaapps.android.libs.logger.ALog;
import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.dao.MediaDAO;
import com.chute.android.photopickerplus.ui.fragment.AccountFilesListener;
import com.chute.android.photopickerplus.ui.fragment.CursorFilesListener;
import com.chute.android.photopickerplus.ui.fragment.FragmentServices;
import com.chute.android.photopickerplus.ui.fragment.FragmentServices.ServiceClickedListener;
import com.chute.android.photopickerplus.util.AppUtil;
import com.chute.android.photopickerplus.util.Constants;
import com.chute.android.photopickerplus.util.NotificationUtil;
import com.chute.android.photopickerplus.util.PhotoFilterType;
import com.chute.android.photopickerplus.util.PhotoPickerPreferenceUtil;
import com.chute.android.photopickerplus.util.intent.IntentUtil;
import com.chute.android.photopickerplus.util.intent.PhotoPickerPlusIntentWrapper;
import com.chute.android.photopickerplus.util.intent.PhotosIntentWrapper;
import com.chute.sdk.v2.api.accounts.GCAccounts;
import com.chute.sdk.v2.api.authentication.AuthenticationFactory;
import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.AccountModel;
import com.chute.sdk.v2.model.enums.AccountType;
import com.chute.sdk.v2.model.enums.Service;
import com.chute.sdk.v2.model.response.ListResponseModel;
import com.chute.sdk.v2.utils.PreferenceUtil;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class ServicesActivity extends FragmentActivity implements AccountFilesListener,
    CursorFilesListener,
    ServiceClickedListener {

  private static final String TAG = ServicesActivity.class.getSimpleName();

  private Service serviceType;
  private AccountType accountType;
  private PhotoPickerPlusIntentWrapper ppWrapper;

  private FragmentServices fragmentServicesVertical;

  private static FragmentManager fragmentManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fragmentManager = getSupportFragmentManager();
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.main_layout);

    ppWrapper = new PhotoPickerPlusIntentWrapper(getIntent());
    fragmentServicesVertical = (FragmentServices) fragmentManager
        .findFragmentById(R.id.fragmentServices);

    ArrayList<Service> serviceList = new ArrayList<Service>();
    if (PhotoPickerPreferenceUtil.get().hasAccountName(Service.FACEBOOK)) {
      serviceList.add(Service.FACEBOOK);
    }
    if (PhotoPickerPreferenceUtil.get().hasAccountName(Service.FLICKR)) {
      serviceList.add(Service.FLICKR);
    }
    if (PhotoPickerPreferenceUtil.get().hasAccountName(Service.PICASA)) {
      serviceList.add(Service.PICASA);
    }
    if (PhotoPickerPreferenceUtil.get().hasAccountName(Service.INSTAGRAM)) {
      serviceList.add(Service.INSTAGRAM);
    }
    if (PhotoPickerPreferenceUtil.get().hasAccountName(Service.ALL_PHOTOS)) {
      serviceList.add(Service.ALL_PHOTOS);
    }
    if (PhotoPickerPreferenceUtil.get().hasAccountName(Service.CAMERA_SHOTS)) {
      serviceList.add(Service.CAMERA_SHOTS);
    }
    if (PhotoPickerPreferenceUtil.get().hasAccountName(Service.LAST_PHOTO_TAKEN)) {
      serviceList.add(Service.LAST_PHOTO_TAKEN);
    }
    if (PhotoPickerPreferenceUtil.get().hasAccountName(Service.TAKE_PHOTO)) {
      serviceList.add(Service.TAKE_PHOTO);
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
      intent.putExtra(MediaStore.EXTRA_OUTPUT,
          Uri.fromFile(AppUtil.getTempFile(ServicesActivity.this)));
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
    wrapper.startActivityForResult(ServicesActivity.this,
        PhotosIntentWrapper.ACTIVITY_FOR_RESULT_STREAM_KEY);

  }

  @Override
  public void lastPhoto() {
    Uri uri = MediaDAO.getLastPhotoFromCameraPhotos(getApplicationContext());
    if (uri.toString().equals("")) {
      NotificationUtil.makeToast(getApplicationContext(),
          getResources().getString(R.string.no_camera_photos));
    } else {
      final AccountMediaModel model = new AccountMediaModel();
      model.setThumbnail(uri.toString());
      model.setImageUrl(uri.toString());

      IntentUtil.deliverDataToInitialActivity(ServicesActivity.this, model);
    }

  }

  @Override
  public void cameraRoll() {
    final PhotosIntentWrapper wrapper = new PhotosIntentWrapper(ServicesActivity.this);
    wrapper.setMultiPicker(ppWrapper.getIsMultiPicker());
    wrapper.setFilterType(PhotoFilterType.CAMERA_ROLL);
    wrapper.setChuteId(ppWrapper.getAlbumId());
    wrapper.startActivityForResult(ServicesActivity.this,
        PhotosIntentWrapper.ACTIVITY_FOR_RESULT_STREAM_KEY);

  }

  @Override
  public void accountLogin(Service type) {
    serviceType = type;
    if (PreferenceUtil.get().hasAccount(serviceType.getLabel())) {
      AccountModel account = PreferenceUtil.get()
          .getAccount(serviceType.getLabel());
      accountClicked(account.getId(), account.getType(), account.getShortcut());
    } else {
      PhotoPickerPreferenceUtil.get().setAccountType(serviceType.getLabel());
      accountType = AccountType.valueOf(serviceType.name());
      AuthenticationFactory.getInstance().startAuthenticationActivity(
          ServicesActivity.this, accountType);
    }

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      GCAccounts.allUserAccounts(getApplicationContext(), new AccountsCallback())
          .executeAsync();
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
            path = MediaDAO.getLastPhotoFromCameraPhotos(getApplicationContext())
                .toString();
          } catch (FileNotFoundException e) {
            Log.d(TAG, "", e);
          }
        } else {
          Log.e(TAG, "Bug " + data.getData().getPath());
          path = Uri.fromFile(
              new File(AppUtil.getPath(getApplicationContext(), data.getData())))
              .toString();
        }
        Log.d(TAG, path);
        final AccountMediaModel model = new AccountMediaModel();
        model.setThumbnail(path);
        model.setImageUrl(path);

        IntentUtil.deliverDataToInitialActivity(this, model);
      }
    }
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setResult(Activity.RESULT_OK, new Intent().putExtras(intent.getExtras()));
    finish();
  }

  private final class AccountsCallback implements
      HttpCallback<ListResponseModel<AccountModel>> {

    @Override
    public void onSuccess(ListResponseModel<AccountModel> responseData) {
      if (accountType == null) {
        // return;
        String type = PhotoPickerPreferenceUtil.get().getAccountType();
        accountType = AccountType.valueOf(type);
      }
      if (responseData.getData().size() == 0) {
        Toast.makeText(getApplicationContext(),
            getResources().getString(R.string.no_albums_found),
            Toast.LENGTH_SHORT).show();
        return;
      }
      AccountModel accountModel = responseData.getData().get(0);
      PreferenceUtil.get().saveAccount(accountModel);
      accountClicked(accountModel.getId(), accountModel.getType(),
          accountModel.getShortcut());
    }

    @Override
    public void onHttpError(ResponseStatus responseStatus) {
      ALog.d("Http Error: " + responseStatus.getStatusCode() + " "
          + responseStatus.getStatusMessage());
    }

  }

  public void accountClicked(String accountId, String accountName, String accountShortcut) {
    final PhotosIntentWrapper wrapper = new PhotosIntentWrapper(ServicesActivity.this);
    wrapper.setMultiPicker(ppWrapper.getIsMultiPicker());
    wrapper.setFilterType(PhotoFilterType.SOCIAL_PHOTOS);
    wrapper.setAccountId(accountId);
    wrapper.setAccountName(accountName);
    wrapper.setAccountShortcut(accountShortcut);
    wrapper.startActivityForResult(ServicesActivity.this,
        PhotosIntentWrapper.ACTIVITY_FOR_RESULT_STREAM_KEY);

  }

  @Override
  public void onDeliverAccountFiles(ArrayList<AccountMediaModel> accountMediaModelList) {
    IntentUtil.deliverDataToInitialActivity(ServicesActivity.this, accountMediaModelList);

  }

  @Override
  public void onDeliverCursorAssets(ArrayList<String> assetPathList) {
    IntentUtil.deliverDataToInitialActivity(ServicesActivity.this,
        AppUtil.getPhotoCollection(assetPathList));

  }

  @Override
  public void onAccountFilesSelect(AccountMediaModel accountMediaModel) {
    IntentUtil.deliverDataToInitialActivity(ServicesActivity.this, accountMediaModel);
  }

  @Override
  public void onCursorAssetsSelect(AccountMediaModel accountMediaModel) {
    IntentUtil.deliverDataToInitialActivity(ServicesActivity.this, accountMediaModel);
  }

  @Override
  public void onAccountFolderSelect(String accountType, String accountShortcut,
      String folderId, boolean isMultipicker) {
    // TODO Auto-generated method stub

  }

}
