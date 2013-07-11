package com.chute.android.photopickerplus.util;

import android.content.Context;

import com.chute.sdk.v2.model.AccountStore;
import com.chute.sdk.v2.model.enums.TokenType;
import com.dg.libs.rest.client.BaseRestClient;

public class TokenAuthentication {

	public static void authenticate(Context context, String token) {
		AccountStore account = AccountStore.getInstance(context);
		account.setPassword(token);
		account.setTokenType(TokenType.ACCESS_TOKEN);
		BaseRestClient.setDefaultAuthenticationProvider(account);
	}

}
