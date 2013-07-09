package com.chute.android.photopickerplus.util;

import com.chute.sdk.v2.api.authentication.AuthenticationFactory.AccountType;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class PhotoPickerPreferenceUtil {

	public static final String TAG = PhotoPickerPreferenceUtil.class.getSimpleName();
	private final Context context;

	private PhotoPickerPreferenceUtil(Context context) {
		this.context = context;
	}

	static PhotoPickerPreferenceUtil instance;

	public static PhotoPickerPreferenceUtil get() {
		return instance;
	}

	public static void init(Context context) {
		if (instance == null) {
			instance = new PhotoPickerPreferenceUtil(context.getApplicationContext());
		}
	}

	public SharedPreferences getPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	private final <T> void setPreference(final String key, final T value) {
		SharedPreferences.Editor edit = getPreferences().edit();
		if (value.getClass().equals(String.class)) {
			edit.putString(key, (String) value);
		} else if (value.getClass().equals(Boolean.class)) {
			edit.putBoolean(key, (Boolean) value);
		} else if (value.getClass().equals(Integer.class)) {
			edit.putInt(key, (Integer) value);
		} else if (value.getClass().equals(Long.class)) {
			edit.putLong(key, (Long) value);
		} else if (value.getClass().equals(Float.class)) {
			edit.putFloat(key, (Float) value);
		} else {
			throw new UnsupportedOperationException(
					"Need to add a primitive type to shared prefs");
		}
		edit.commit();
	}

	// Account ID
	public void setIdForAccount(AccountType accountType, String accountId) {
		setPreference(accountType.getName(), accountId);
	}

	public boolean hasAccountId(AccountType accountType) {
		return getPreferences().contains(accountType.getName());
	}

	public String getAccountId(AccountType accountType) {
		return getPreferences().getString(accountType.getName(), null);
	}

	// Account Name
	public void setNameForAccount(AccountType accountType, String accountName) {
		setPreference(accountType.getName() + "_name", accountName);
	}

	public boolean hasAccountName(AccountType accountType) {
		return getPreferences().contains(accountType.getName() + "_name");
	}

	public String getAccountName(AccountType accountType) {
		return getPreferences()
				.getString(accountType.getName() + "_name", null);
	}

	// Account UID
	public void setUidForAccount(AccountType accountType, String uid) {
		setPreference(accountType.getName() + "__name", uid);
	}

	public boolean hasUid(AccountType accountType) {
		return getPreferences().contains(accountType.getName() + "__name");
	}

	public String getUidForAccount(AccountType accountType) {
		return getPreferences().getString(accountType.getName() + "__name",
				null);
	}

}
