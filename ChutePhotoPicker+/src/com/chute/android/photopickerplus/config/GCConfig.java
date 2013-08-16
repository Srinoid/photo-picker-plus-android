package com.chute.android.photopickerplus.config;

import android.content.Context;

import com.dg.libs.rest.HttpRequest;
import com.dg.libs.rest.callbacks.HttpCallback;

public class GCConfig {

	public static HttpRequest getConfigurableServices(final Context context, final HttpCallback<ServiceResponseModel<String>> callback) {
		return new ConfigServicesRequest(context, callback);
	}

}
