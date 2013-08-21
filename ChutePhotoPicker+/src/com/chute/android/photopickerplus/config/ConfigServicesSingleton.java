package com.chute.android.photopickerplus.config;

import java.util.ArrayList;

import android.content.Context;

import com.araneaapps.android.libs.logger.ALog;
import com.chute.android.photopickerplus.util.PhotoPickerPreferenceUtil;
import com.chute.sdk.v2.model.enums.Service;
import com.dg.libs.rest.HttpRequest;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class ConfigServicesSingleton {

  private static ConfigServicesSingleton instance;
  private Context context;

  public static ConfigServicesSingleton getInstance(Context context) {
    if (instance == null) {
      instance = new ConfigServicesSingleton(context);
    }
    return instance;
  }

  private ConfigServicesSingleton(Context context) {
    this.context = context;
  }

  public void setAvailableServices(ArrayList<Service> services) {
    for (Service service : services) {
      PhotoPickerPreferenceUtil.get().setNameForAccount(service,
          service.getLabel());
    }
  }

  public void fetchConfigFromServer() {
    getConfigurableServices(context, new ConfigServicesCallback()).executeAsync();
  }

  private final class ConfigServicesCallback implements
      HttpCallback<ServiceResponseModel> {

    @Override
    public void onHttpError(ResponseStatus status) {
      ALog.d("Http Error = " + status.getStatusMessage() + " " + status.getStatusCode());

    }

    @Override
    public void onSuccess(ServiceResponseModel data) {
      ALog.d("Response = " + data.toString());
      ArrayList<Service> services = new ArrayList<Service>();
      for (String serviseResponseModel : data.getServices()) {
        Service service = Service.valueOf(serviseResponseModel.toUpperCase());
        services.add(service);
      }
      setAvailableServices(services);

    }

  }

  private HttpRequest getConfigurableServices(final Context context,
      final HttpCallback<ServiceResponseModel> callback) {
    return new ConfigServicesRequest(context, callback);
  }

}
