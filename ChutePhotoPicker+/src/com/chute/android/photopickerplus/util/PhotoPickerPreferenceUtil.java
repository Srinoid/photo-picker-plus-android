// Copyright (c) 2011, Chute Corporation. All rights reserved.
// 
//  Redistribution and use in source and binary forms, with or without modification, 
//  are permitted provided that the following conditions are met:
// 
//     * Redistributions of source code must retain the above copyright notice, this 
//       list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright notice,
//       this list of conditions and the following disclaimer in the documentation
//       and/or other materials provided with the distribution.
//     * Neither the name of the  Chute Corporation nor the names
//       of its contributors may be used to endorse or promote products derived from
//       this software without specific prior written permission.
// 
//  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
//  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
//  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
//  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
//  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
//  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
//  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
//  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
//  OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
//  OF THE POSSIBILITY OF SUCH DAMAGE.
//
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
  private static final String KEY_CLIENT_ID = "clientId";
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

  public void setClientId(String clientId) {
    setPreference(KEY_CLIENT_ID, clientId);
  }

  public String getClientId() {
    return getPreferences().getString(KEY_CLIENT_ID, null);
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
