package com.chute.android.photopickerplus.api;

import com.chute.android.photopickerplus.model.AccountMediaModel;
import com.chute.android.photopickerplus.model.AccountModel;
import com.chute.android.photopickerplus.model.AccountObjectModel;
import com.chute.sdk.v2.model.response.ListResponseModel;
import com.dg.libs.rest.HttpRequest;
import com.dg.libs.rest.callbacks.HttpCallback;

import android.content.Context;

/**
 * The {@link GCAccounts} is a helper class which contains methods for getting
 * accounts, list of objects for the specific account, as well as media items
 * for the specific object.
 * 
 */
public class GCAccounts {

	public static final String TAG = GCAccounts.class.getSimpleName();

	/**
	 * Method used for getting accounts. It returns a JSon object containing a
	 * list of accounts using the following parameters: context, given parser
	 * and given callback.
	 * 
	 * @param context
	 *            The application context.
	 * @param parser
	 *            Instance of {@link GCHttpResponseParser} interface. You can
	 *            add a custom parser or use the parser provided here {@see
	 *            #all(Context, GCHttpCallback)}.
	 * @param callback
	 *            Instance of {@link GCHttpCallback} interface. According to the
	 *            parser, the callback should have the same return type.
	 * @return Instance of {@link AccountsGetRequest}, class that implements
	 *         {@link GCHttpRequest}.
	 */
	public static HttpRequest all(final Context context, final HttpCallback<ListResponseModel<AccountModel>> callback) {
		return new AccountsGetRequest(context, callback);
	}

	/**
	 * Method used for getting list of object for a specific account. It returns
	 * a JSon object using the following parameters: context, string value, the
	 * given callback and parser.
	 * 
	 * @param context
	 *            The application context.
	 * @param accountId
	 *            {@link AccountModel} ID, representing the specific account.
	 * @param parser
	 *            Instance of {@link GCHttpResponseParser} interface. You can
	 *            add a custom parser or use the parser provided here {@see
	 *            #objectMedia(Context, String, String, GCHttpCallback)}.
	 * @param callback
	 *            Instance of {@link GCHttpCallback} interface. According to the
	 *            parser, the callback should have the same return type.
	 * @return Instance of {@link AccountsGetObjectsRequest}, class that
	 *         implements {@link GCHttpRequest}.
	 */
	public static HttpRequest objects(final Context context, final String accountId,
			final HttpCallback<ListResponseModel<AccountObjectModel>> callback) {
		return new AccountsGetObjectsRequest(context, accountId, callback);
	}

	/**
	 * Method used for getting media items for a specific object. It returns
	 * JSon object using the following parameters: context, string values, the
	 * given callback and parser.
	 * 
	 * @param context
	 *            The application context.
	 * @param accountId
	 *            {@link AccountModel} ID, representing the specific account.
	 * @param objectId
	 *            {@link AccountObjectModel} ID, representing the specific
	 *            object from an account.
	 * @param parser
	 *            Instance of {@link GCHttpResponseParser} interface. You can
	 *            add a custom parser or use the parser provided here {@see
	 *            #objectMedia(Context, String, String, GCHttpCallback)}.
	 * @param callback
	 *            Instance of {@link GCHttpCallback} interface. According to the
	 *            parser, the callback should have the same return type.
	 * @return Instance of {@link AccountsGetObjectMediaRequest}, class that
	 *         implements {@link GCHttpRequest}.
	 */
	public static HttpRequest objectMedia(final Context context, final String accountId, final String objectId,
			final HttpCallback<ListResponseModel<AccountMediaModel>> callback) {
		return new AccountsGetObjectMediaRequest(context, accountId, objectId, callback);
	}
}
