/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
import com.chute.sdk.v2.api.Chute;
import com.chute.sdk.v2.api.authentication.AuthConstants;
import com.chute.sdk.v2.api.authentication.AuthenticationFactory;
import com.chute.sdk.v2.api.authentication.TokenAuthenticationProvider;
import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.AssetModel;
import com.chute.sdk.v2.model.response.ResponseModel;
import com.dg.libs.rest.HttpRequest;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

/**
 * The {@link ImageDataResponseLoader} has the responsibility for uploading the
 * selected photo(s) URL(s) and delivering the retrieved data to the main
 * activity i.e. the activity that started the PhotoPicker.
 * 
 * This class consists exclusively of static methods and is used only with
 * photos from social accounts.
 * 
 */
public class ImageDataResponseLoader {

  public static void postImageData(Context context,
      ArrayList<AccountMediaModel> selectedImages, AccountFilesListener accountListener) {

    String token = TokenAuthenticationProvider.getInstance().getToken();
    AuthConstants authConstants = AuthenticationFactory.getInstance().getAuthConstants();
    String clientId = authConstants.clientId;
    String clientSecret = authConstants.clientSecret;
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
        List<AssetModel> assetList = responseData.getData().getAssetList();
        listener.onDeliverAccountFiles((ArrayList<AssetModel>) assetList);
      }

    }
  }

}
