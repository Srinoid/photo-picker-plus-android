package com.chute.android.photopickerplus.callback;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Toast;

import com.araneaapps.android.libs.logger.ALog;
import com.chute.android.photopickerplus.models.ImageResponseModel;
import com.chute.android.photopickerplus.models.MediaDataModel;
import com.chute.android.photopickerplus.models.MediaModel;
import com.chute.android.photopickerplus.models.OptionsModel;
import com.chute.android.photopickerplus.ui.fragment.AccountFilesListener;
import com.chute.android.photopickerplus.util.PhotoPickerPreferenceUtil;
import com.chute.sdk.v2.api.Chute;
import com.chute.sdk.v2.api.authentication.AuthConstants;
import com.chute.sdk.v2.api.authentication.TokenAuthenticationProvider;
import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.AssetModel;
import com.chute.sdk.v2.model.response.ResponseModel;
import com.dg.libs.rest.HttpRequest;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class ImageDataResponseLoader {

  public static void postImageData(Context context,
      ArrayList<AccountMediaModel> selectedImages, AccountFilesListener accountListener) {

    String token = TokenAuthenticationProvider.getInstance().getToken();
    String clientId = PhotoPickerPreferenceUtil.get().getClientId();
    String clientSecret = PhotoPickerPreferenceUtil.get().getClientSecret();
    Chute.init(context, new AuthConstants(clientId, clientSecret), token);

    ArrayList<MediaDataModel> mediaModelList = new ArrayList<MediaDataModel>();
    for (AccountMediaModel media : selectedImages) {
      MediaDataModel mediaModel = new MediaDataModel();
      mediaModel.setImageUrl(media.getImageUrl());
      mediaModel.setThumbnail(media.getThumbnail());
      mediaModelList.add(mediaModel);
    }

    OptionsModel options = new OptionsModel();
    options.setCliendId(clientId);
    MediaModel imageDataModel = new MediaModel();
    imageDataModel.setOptions(options);
    imageDataModel.setMedia(mediaModelList);

    getImageData(context, imageDataModel,
        new ImageDataCallback(context, accountListener)).executeAsync();

  }

  private static HttpRequest getImageData(final Context context, MediaModel imageData,
      final HttpCallback<ResponseModel<ImageResponseModel>> callback) {
    return new ImageDataRequest(context, imageData, callback);
  }

  private static final class ImageDataCallback implements
      HttpCallback<ResponseModel<ImageResponseModel>> {

    private Context context;
    private AccountFilesListener listener;

    private ImageDataCallback(Context context, AccountFilesListener listener) {
      this.context = context;
      this.listener = listener;
    }

    @Override
    public void onHttpError(ResponseStatus responseStatus) {
      Toast.makeText(context, "An error occurred, please try again later",
          Toast.LENGTH_SHORT).show();
      ALog.d("Http Error: " + responseStatus.getStatusCode() + " "
          + responseStatus.getStatusMessage());

    }

    @Override
    public void onSuccess(ResponseModel<ImageResponseModel> responseData) {
      if (responseData.getData() != null) {
        List<AssetModel> assetList = responseData.getData().getAssetModel();
        ALog.d("Asset List: " + assetList.toString());
        listener.onDeliverAccountFiles((ArrayList<AssetModel>) assetList);
      }

    }

  }

}
