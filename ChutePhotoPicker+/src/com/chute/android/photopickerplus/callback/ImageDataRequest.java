package com.chute.android.photopickerplus.callback;

import android.content.Context;

import com.chute.android.photopickerplus.models.ImageResponseModel;
import com.chute.android.photopickerplus.models.MediaModel;
import com.chute.android.photopickerplus.util.Constants;
import com.chute.sdk.v2.api.parsers.ResponseParser;
import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.response.ResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.requests.StringBodyHttpRequestImpl;

public class ImageDataRequest extends
    StringBodyHttpRequestImpl<ResponseModel<ImageResponseModel>> {

  private MediaModel imageData;

  public ImageDataRequest(Context context, MediaModel imageData,
      HttpCallback<ResponseModel<ImageResponseModel>> callback) {
    super(context, RequestMethod.POST, new ResponseParser<ImageResponseModel>(
        AccountMediaModel.class),
        callback);
    if (imageData == null) {
      throw new IllegalArgumentException("Need to provide image data");
    }
    this.imageData = imageData;
    getClient().addHeader("Content-Type", "application/json");
  }

  @Override
  public String bodyContents() {
    return this.imageData.serializeImageDataModel();
  }

  @Override
  protected String getUrl() {
    return Constants.SELECTED_IMAGES_URL;
  }

}
