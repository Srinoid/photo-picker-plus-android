/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.chute.sdk.api.authentication.GCAuthenticationFactory.AccountType;

public class PreferenceUtil {
    private static final String TAG = PreferenceUtil.class.getSimpleName();
    private final Context context;

    private PreferenceUtil(Context context) {
        this.context = context;
    }

    static PreferenceUtil instance;

    public static PreferenceUtil get() {
        return instance;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new PreferenceUtil(context.getApplicationContext());
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
            throw new UnsupportedOperationException("Need to add a primitive type to shared prefs");
        }
        edit.commit();
    }

    //Account ID
    public void setIdForAccount(AccountType accountType, String accountId) {
        setPreference(accountType.getName(), accountId);
    }

    public boolean hasAccountId(AccountType accountType) {
        return getPreferences().contains(accountType.getName());
    }

    public String getAccountId(AccountType accountType) {
        return getPreferences().getString(accountType.getName(), null);
    }
    
    //Account Name
    public void setNameForAccount(AccountType accountType, String accountName) {
        setPreference(accountType.getName()+"_name", accountName);
    }
    
    public boolean hasAccountName(AccountType accountType) {
    	return getPreferences().contains(accountType.getName()+"_name");
    }
    
    public String getAccountName(AccountType accountType) {
    	return getPreferences().getString(accountType.getName()+"_name", null);
    }
    
}
