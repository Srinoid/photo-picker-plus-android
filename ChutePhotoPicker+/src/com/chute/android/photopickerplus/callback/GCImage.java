package com.chute.android.photopickerplus.callback;

import android.content.Context;

import com.chute.android.photopickerplus.models.ImageDataModel;
import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.response.ListResponseModel;
import com.dg.libs.rest.HttpRequest;
import com.dg.libs.rest.callbacks.HttpCallback;

public class GCImage {

  public static HttpRequest getImageData(final Context context, ImageDataModel imageData,
      final HttpCallback<ListResponseModel<AccountMediaModel>> callback) {
    return new ImageDataRequest(context, imageData, callback);
  }

}
