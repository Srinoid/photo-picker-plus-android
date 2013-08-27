package com.chute.android.photopickerplus.callback;

import android.content.Context;

import com.chute.android.photopickerplus.models.ImageDataModel;
import com.chute.android.photopickerplus.util.Constants;
import com.chute.sdk.v2.api.parsers.ListResponseParser;
import com.chute.sdk.v2.model.response.ListResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.requests.StringBodyHttpRequestImpl;

public class ImageDataRequest extends
    StringBodyHttpRequestImpl<ListResponseModel<String>> {

  private ImageDataModel imageData;

  public ImageDataRequest(Context context, ImageDataModel imageData,
      HttpCallback<ListResponseModel<String>> callback) {
    super(context, RequestMethod.POST, new ListResponseParser<String>(String.class),
        callback);
    if (imageData == null) {
      throw new IllegalArgumentException("Need to provide image data");
    }
    this.imageData = imageData;
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
