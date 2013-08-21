package com.chute.android.photopickerplus.config;

import java.util.Arrays;
import java.util.List;

import android.content.Context;

import com.araneaapps.android.libs.logger.ALog;
import com.chute.android.photopickerplus.models.enums.LocalMediaType;
import com.chute.sdk.v2.model.enums.AccountType;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class ConfigServicesSingleton {

  private static ConfigServicesSingleton instance;
  private Context context;
  private List<AccountType> remoteServices;
  private List<LocalMediaType> localServices;

  public static ConfigServicesSingleton getInstance(Context context) {
    if (instance == null) {
      instance = new ConfigServicesSingleton(context);
    }
    return instance;
  }

  private ConfigServicesSingleton(Context context) {
    this.context = context;
  }

  public void setAvailableRemoteServices(AccountType... remoteServices) {
    this.remoteServices = Arrays.asList(remoteServices);
  }

  public void setAvailableLocalServices(LocalMediaType... localServices) {
    this.localServices = Arrays.asList(localServices);

  }

  public void fetchConfigFromServer(String url) {
    new ConfigServicesRequest(context, url, new ConfigServicesCallback()).executeAsync();
  }

  public List<LocalMediaType> getLocalServices() {
    return localServices;
  }

  public List<AccountType> getRemoteServices() {
    return remoteServices;
  }

  private final class ConfigServicesCallback implements
      HttpCallback<ServiceResponseModel> {

    @Override
    public void onHttpError(ResponseStatus status) {
      ALog.d("Http Error = " + status.getStatusMessage() + " " + status.getStatusCode());
    }

    @Override
    public void onSuccess(ServiceResponseModel data) {
      // setAvailableServices(services);
      // You will have 2 different things in the response. Remote Services or
      // Local services. Parse for that.
    }

  }

}
