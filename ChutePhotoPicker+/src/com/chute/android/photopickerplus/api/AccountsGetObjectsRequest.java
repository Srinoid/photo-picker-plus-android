package com.chute.android.photopickerplus.api;

import android.content.Context;
import android.text.TextUtils;

import com.chute.android.photopickerplus.model.AccountObjectModel;
import com.chute.android.photopickerplus.util.Constants;
import com.chute.sdk.v2.api.parsers.ListResponseParser;
import com.chute.sdk.v2.model.response.ListResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.requests.ParameterHttpRequestImpl;

public class AccountsGetObjectsRequest extends ParameterHttpRequestImpl<ListResponseModel<AccountObjectModel>> {

	public static final String TAG = AccountsGetObjectsRequest.class.getSimpleName();
	private final String accountShortcut;

	public AccountsGetObjectsRequest(Context context, String accountShortcut,
			HttpCallback<ListResponseModel<AccountObjectModel>> callback) {
		super(context, RequestMethod.GET, new ListResponseParser<AccountObjectModel>(AccountObjectModel.class),
				callback);
		if (TextUtils.isEmpty(accountShortcut)) {
			throw new NullPointerException("Need to provide account shortcut");
		}
		this.accountShortcut = accountShortcut;
	}

	@Override
	protected String getUrl() {
		return String.format(Constants.URL_ACCOUNT_OBJECT, accountShortcut);
	}

}
