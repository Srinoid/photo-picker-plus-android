package com.chute.sdk.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.chute.sdk.api.authentication.GCAuthenticationFactory.AccountType;

public class GCPreferenceUtil {

	public static final String TAG = GCPreferenceUtil.class.getSimpleName();
	private static final String KEY_LAUNCH_WIZARD = "key_should_launch_wizard";
	private final Context context;

	private GCPreferenceUtil(Context context) {
		this.context = context;
	}

	static GCPreferenceUtil instance;

	public static GCPreferenceUtil get() {
		return instance;
	}

	public static void init(Context context) {
		if (instance == null) {
			instance = new GCPreferenceUtil(context.getApplicationContext());
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

	// Should launch wizard
	public boolean shouldLaunchWizard() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getBoolean(KEY_LAUNCH_WIZARD, true);
	}

	public void setShouldLaunchWizard(boolean value) {
		setPreference(KEY_LAUNCH_WIZARD, value);
	}

}
