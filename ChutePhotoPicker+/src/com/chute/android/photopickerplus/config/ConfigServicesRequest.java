package com.chute.android.photopickerplus.config;

import android.content.Context;

import com.chute.android.photopickerplus.util.PhotoPickerPreferenceUtil;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.requests.ParameterHttpRequestImpl;

public class ConfigServicesRequest extends ParameterHttpRequestImpl<ServiceResponseModel<String>> {

	public ConfigServicesRequest(Context context, HttpCallback<ServiceResponseModel<String>> callback) {
		super(context, RequestMethod.GET, new ServiceResponseParser<String>(String.class), callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getUrl() {
		return PhotoPickerPreferenceUtil.get().getConfigUrl();
	}

}
