/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.util.intent;

import java.util.ArrayList;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.chute.android.photopickerplus.ui.activity.ServicesActivity;
import com.chute.sdk.v2.model.AccountMediaModel;

public class IntentUtil {

	public static void deliverDataToInitialActivity(final FragmentActivity context, final AccountMediaModel model,
			final String chuteId) {
		deliverDataToInitialActivity(context, model, null, null, chuteId);
	}

	public static void deliverDataToInitialActivity(final FragmentActivity context, final AccountMediaModel model,
			final String albumId, final String accountId, final String chuteId) {
		ArrayList<AccountMediaModel> mediaCollection = new ArrayList<AccountMediaModel>();
		mediaCollection.add(model);
		deliverDataToInitialActivity(context, mediaCollection, chuteId);
	}

	public static void deliverDataToInitialActivity(final FragmentActivity context,
			final ArrayList<AccountMediaModel> collection, final String chuteId) {
		deliverDataToInitialActivity(context, collection, null, null, chuteId);
	}

	public static void deliverDataToInitialActivity(final FragmentActivity context,
			final ArrayList<AccountMediaModel> collection, final String albumId, final String accountId,
			final String chuteId) {
		final GridActivityIntentWrapper wrapper = new GridActivityIntentWrapper(new Intent(context,
				ServicesActivity.class));
		if (!TextUtils.isEmpty(accountId)) {
			wrapper.setAccountId(accountId);
		}
		if (!TextUtils.isEmpty(albumId)) {
			wrapper.setAlbumId(albumId);
		}
		wrapper.setChuteId(chuteId);
		wrapper.setMediaCollection(collection);
		wrapper.getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		wrapper.startActivity(context);
	}
}
