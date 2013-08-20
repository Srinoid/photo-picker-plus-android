package com.chute.android.photopickerplus.config;

import android.content.Context;

import com.chute.android.photopickerplus.util.PhotoPickerPreferenceUtil;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.requests.ParameterHttpRequestImpl;

public class ConfigServicesRequest extends ParameterHttpRequestImpl<ServiceResponseModel> {

  public ConfigServicesRequest(Context context,
      HttpCallback<ServiceResponseModel> callback) {
    super(context, RequestMethod.GET, new ServiceResponseParser(), callback);
  }

  @Override
  protected String getUrl() {
    return PhotoPickerPreferenceUtil.get().getConfigUrl();
  }

}
