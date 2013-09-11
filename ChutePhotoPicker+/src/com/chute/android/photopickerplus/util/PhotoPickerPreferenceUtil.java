/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.util;

import java.util.ArrayList;

import com.chute.android.photopickerplus.models.enums.LocalMediaType;
import com.chute.sdk.v2.model.enums.AccountType;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PhotoPickerPreferenceUtil {

  public static final String TAG = PhotoPickerPreferenceUtil.class.getSimpleName();
  private static final String KEY_ACCOUNT_TYPE = "accountType";
  private static final String KEY_ACCOUNT_SERVICE_LIST = "accountServiceList";
  private static final String KEY_LOCAL_SERVICE_LIST = "localServiceList";
  private final Context context;

  private PhotoPickerPreferenceUtil(Context context) {
    this.context = context;
  }

  static PhotoPickerPreferenceUtil instance;

  public static PhotoPickerPreferenceUtil get() {
    return instance;
  }

  public static boolean isInitialized() {
    return instance != null;
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

  public void setAccountType(AccountType accountType) {
    setPreference(KEY_ACCOUNT_TYPE, accountType.name());
  }

  public AccountType getAccountType() {
    String accountName = getPreferences().getString(KEY_ACCOUNT_TYPE, null);
    AccountType type = null;
    for (AccountType accountType : AccountType.values()) {
      if (accountName.equalsIgnoreCase(accountType.name())) {
        type = accountType;
      }
    }
    return type;
  }

  public void setAccountServiceList(ArrayList<AccountType> accountList) {
    for (AccountType accountType : accountList) {
      setPreference(KEY_ACCOUNT_SERVICE_LIST + "_" + accountType, accountType.name());
    }
  }

  public ArrayList<AccountType> getAccountServiceList() {
    ArrayList<AccountType> accountList = new ArrayList<AccountType>();
    for (AccountType accountType : AccountType.values()) {
      if (getPreferences().contains(KEY_ACCOUNT_SERVICE_LIST + "_" + accountType)) {
        accountList.add(accountType);
      }
    }
    return accountList;
  }

  public void setLocalServiceList(ArrayList<LocalMediaType> localServiceList) {
    for (LocalMediaType localMediaType : localServiceList) {
      setPreference(KEY_LOCAL_SERVICE_LIST + "_" + localMediaType, localMediaType.name());
    }
  }

  public ArrayList<LocalMediaType> getLocalServiceList() {
    ArrayList<LocalMediaType> localServiceList = new ArrayList<LocalMediaType>();
    for (LocalMediaType localMediaType : LocalMediaType.values()) {
      if (getPreferences().contains(KEY_LOCAL_SERVICE_LIST + "_" + localMediaType)) {
        localServiceList.add(localMediaType);
      }
    }
    return localServiceList;
  }

}
