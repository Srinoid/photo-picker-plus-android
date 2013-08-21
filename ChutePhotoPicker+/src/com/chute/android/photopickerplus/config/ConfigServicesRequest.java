package com.chute.android.photopickerplus.config;

import android.content.Context;

import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.requests.ParameterHttpRequestImpl;

public class ConfigServicesRequest extends ParameterHttpRequestImpl<ServiceResponseModel> {

  private String url;

  public ConfigServicesRequest(Context context, String url,
      HttpCallback<ServiceResponseModel> callback) {
    super(context, RequestMethod.GET, new ServiceResponseParser(), callback);
    this.url = url;
  }

  @Override
  protected String getUrl() {
    return url;
  }

}
