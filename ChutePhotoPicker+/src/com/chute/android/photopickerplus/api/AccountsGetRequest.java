package com.chute.android.photopickerplus.api;

import android.content.Context;

import com.chute.android.photopickerplus.model.AccountModel;
import com.chute.android.photopickerplus.util.Constants;
import com.chute.sdk.v2.api.parsers.ListResponseParser;
import com.chute.sdk.v2.model.response.ListResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.requests.ParameterHttpRequestImpl;

public class AccountsGetRequest extends ParameterHttpRequestImpl<ListResponseModel<AccountModel>> {

	public static final String TAG = AccountsGetRequest.class.getSimpleName();

	public AccountsGetRequest(Context context, HttpCallback<ListResponseModel<AccountModel>> callback) {
		super(context, RequestMethod.GET, new ListResponseParser<AccountModel>(AccountModel.class), callback);
	}

	@Override
	protected String getUrl() {
		return Constants.URL_ACCOUNTS;
	}
}
