package com.chute.android.photopickerplus.config;

import java.util.ArrayList;
import java.util.List;

import com.chute.android.photopickerplus.models.enums.LocalMediaType;
import com.chute.sdk.v2.model.enums.AccountType;

public class DefaultConfigurationFactory {

  public static List<LocalMediaType> createLocalMediaList() {
    List<LocalMediaType> localMediaTypeList = new ArrayList<LocalMediaType>();
    localMediaTypeList.add(LocalMediaType.ALL_PHOTOS);
    localMediaTypeList.add(LocalMediaType.CAMERA_PHOTOS);
    return localMediaTypeList;
  }

  public static List<AccountType> createAccountTypeList() {
    List<AccountType> accountTypeList = new ArrayList<AccountType>();
    accountTypeList.add(AccountType.FACEBOOK);
    accountTypeList.add(AccountType.INSTAGRAM);
    return accountTypeList;
  }
}