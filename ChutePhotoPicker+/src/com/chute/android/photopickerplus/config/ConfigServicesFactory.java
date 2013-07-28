package com.chute.android.photopickerplus.config;

import java.util.ArrayList;

import com.araneaapps.android.libs.logger.ALog;
import com.chute.android.photopickerplus.util.PhotoPickerPreferenceUtil;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

import android.content.Context;

public class ConfigServicesFactory {

	public static final int CONFIG_LOCAL = 1;
	public static final int CONFIG_SERVER = 2;
	private static ConfigServicesFactory instance;
	private Context context;

	public static ConfigServicesFactory getInstance(Context context) {
		if (instance == null) {
			instance = new ConfigServicesFactory(context);
		}
		return instance;
	}

	private ConfigServicesFactory(Context context) {
		this.context = context;
	}

	public void configureServices(int mode, ArrayList<String> services) {
		if (mode == CONFIG_LOCAL) {
			configureServicesLocally(services);
		} else if (mode == CONFIG_SERVER) {
			fetchServicesConfigurationFromServer();
		}
	}

	private void configureServicesLocally(ArrayList<String> services) {
		PhotoPickerPreferenceUtil.get().setServiceList((ArrayList<String>) services);

	}

	private void fetchServicesConfigurationFromServer() {

		GCConfig.getConfigurableServices(context, new ConfigServicesCallback()).executeAsync();

	}

	private final class ConfigServicesCallback implements HttpCallback<ServiceResponseModel<String>> {

		@Override
		public void onHttpError(ResponseStatus status) {
			ALog.d("Http Error = " + status.getStatusMessage() + " " + status.getStatusCode());

		}

		@Override
		public void onSuccess(ServiceResponseModel<String> data) {
			ALog.d("Response = " + data.toString());
			PhotoPickerPreferenceUtil.get().setServiceList((ArrayList<String>) data.getServices());

		}

	}

}
