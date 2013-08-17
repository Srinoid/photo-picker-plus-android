package com.chute.android.photopickerplus.config;

import java.util.ArrayList;

import android.content.Context;

import com.araneaapps.android.libs.logger.ALog;
import com.chute.sdk.v2.model.enums.AccountType;
import com.chute.sdk.v2.utils.PreferenceUtil;
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

	public void setAvailableServices(ArrayList<AccountType> services) {
		for (AccountType accountType : services) {
			PreferenceUtil.get().setNameForAccount(accountType, accountType.getName());
		}
	}

	public void fetchConfigFromServer() {
		getConfigurableServices(context, new ConfigServicesCallback()).executeAsync();
	}

	private final class ConfigServicesCallback implements HttpCallback<ServiceResponseModel> {

		@Override
		public void onHttpError(ResponseStatus status) {
			ALog.d("Http Error = " + status.getStatusMessage() + " " + status.getStatusCode());

		}

		@Override
		public void onSuccess(ServiceResponseModel data) {
			ALog.d("Response = " + data.toString());
			setAvailableServices((ArrayList<AccountType>) data.getServices());

		}

	}

	private HttpRequest getConfigurableServices(final Context context, final HttpCallback<ServiceResponseModel> callback) {
		return new ConfigServicesRequest(context, callback);
	}

}
