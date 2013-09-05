package com.chute.android.photopickerplus.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.araneaapps.android.libs.logger.ALog;
import com.chute.android.photopickerplus.models.enums.LocalMediaType;
import com.chute.android.photopickerplus.util.PhotoPickerPreferenceUtil;
import com.chute.sdk.v2.model.enums.AccountType;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class PhotoPicker {

  private static final String LOG_INIT_CONFIG =
      "Initialize PhotoPicker with configuration";
  private static final String LOG_DESTROY = "Destroy PhotoPicker";

  private static final String WARNING_RE_INIT_CONFIG =
      "Try to initialize PhotoPicker which had already been initialized before. "
          + "To re-init PhotoPicker with new configuration call ServiceLoader.destroy() at first.";
  private static final String ERROR_NOT_INIT =
      "PhotoPicker must be initialized with configuration before using";
  private static final String ERROR_INIT_CONFIG_WITH_NULL =
      "PhotoPicker configuration can not be initialized with null";
  private static final String ERROR_HTTP =
      "Error when trying to get services from server: ";
  private static final String WARNING_UNSUPPORTED_SERVICES = "Invalid service type. Supported valid services: Facebook, Google, Googledrive, Instagram, Flickr, Picasa, Dropbox, Skydrive";

  private List<AccountType> remoteServices;
  private List<LocalMediaType> localServices;

  private PhotoPickerConfiguration configuration;

  private volatile static PhotoPicker instance;

  public static PhotoPicker getInstance() {
    if (instance == null) {
      synchronized (PhotoPicker.class) {
        if (instance == null) {
          instance = new PhotoPicker();
        }
      }
    }
    return instance;
  }

  protected PhotoPicker() {
  }

  public synchronized void init(PhotoPickerConfiguration configuration) {
    if (configuration == null) {
      throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
    }
    if (this.configuration == null) {
      ALog.d(LOG_INIT_CONFIG);
      this.configuration = configuration;
    } else {
      ALog.w(WARNING_RE_INIT_CONFIG);
    }
    fetchConfigFromServer();
  }

  public void fetchConfigFromServer() {
    checkConfiguration();
    if (configuration.configUrl != null) {
      fetchConfigFromServer(configuration.configUrl);
    }
  }

  public List<LocalMediaType> getLocalServices() {
    ArrayList<LocalMediaType> localServiceListInPrefs = PhotoPickerPreferenceUtil.get()
        .getLocalServiceList();
    if (localServiceListInPrefs.isEmpty()) {
      if (configuration.localMediaList != null) {
        return configuration.localMediaList;
      } else {
        return new ArrayList<LocalMediaType>();
      }
    } else {
      return localServiceListInPrefs;
    }
  }

  public List<AccountType> getRemoteServices() {
    ArrayList<AccountType> accountServiceListInPrefs = PhotoPickerPreferenceUtil.get()
        .getAccountServiceList();
    if (accountServiceListInPrefs.isEmpty()) {
      if (configuration.accountList != null) {
        return configuration.accountList;
      } else {
        return new ArrayList<AccountType>();
      }
    } else {
      return accountServiceListInPrefs;
    }
  }

  public void setAvailableRemoteServices(List<AccountType> remoteServices) {
    this.remoteServices = remoteServices;
    checkIfServiceIsSupported(remoteServices);
  }

  public void setAvailableLocalServices(List<LocalMediaType> localServices) {
    this.localServices = localServices;

  }

  public void fetchConfigFromServer(String url) {
    new ServiceRequest(configuration.context, url, new ConfigServicesCallback())
        .executeAsync();
  }

  private final class ConfigServicesCallback implements
      HttpCallback<ServiceResponseModel> {

    @Override
    public void onHttpError(ResponseStatus status) {
      ALog.d(ERROR_HTTP + status.getStatusMessage() + " " + status.getStatusCode());
    }

    @Override
    public void onSuccess(ServiceResponseModel data) {
      remoteServices = new ArrayList<AccountType>();
      localServices = new ArrayList<LocalMediaType>();
      if (data.getServices() != null) {
        for (String service : data.getServices()) {
          AccountType accountType = AccountType.valueOf(service.toUpperCase());
          remoteServices.add(accountType);
        }
        PhotoPickerPreferenceUtil.get().setAccountServiceList(
            (ArrayList<AccountType>) remoteServices);
      }
      if (data.getLocalFeatures() != null) {
        for (String localFeature : data.getLocalFeatures()) {
          LocalMediaType localMediaType = LocalMediaType.valueOf(localFeature
              .toUpperCase());
          localServices.add(localMediaType);
        }
        PhotoPickerPreferenceUtil.get().setLocalServiceList(
            (ArrayList<LocalMediaType>) localServices);
      }
    }

  }

  public boolean isMultiPicker() {
    return configuration.isMultiPicker;

  }

  private void checkConfiguration() {
    if (configuration == null) {
      throw new IllegalStateException(ERROR_NOT_INIT);
    }
  }

  private void checkIfServiceIsSupported(List<AccountType> remoteServices) {
    Iterator<AccountType> iterator = remoteServices.iterator();
    while (iterator.hasNext()) {
      AccountType accountType = iterator.next();
      if (accountType.equals(AccountType.CHUTE)
          || accountType.equals(AccountType.FOURSQUARE)
          || accountType.equals(AccountType.TWITTER)) {
        ALog.w(WARNING_UNSUPPORTED_SERVICES);
        iterator.remove();
      }
    }
  }

  public void destroy() {
    if (configuration != null)
      ALog.d(LOG_DESTROY);
    configuration = null;
  }

}
