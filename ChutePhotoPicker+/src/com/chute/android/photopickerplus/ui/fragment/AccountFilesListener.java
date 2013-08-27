package com.chute.android.photopickerplus.ui.fragment;

import java.util.ArrayList;

import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.enums.AccountType;

public interface AccountFilesListener {

  public void onAccountFilesSelect(AccountMediaModel accountMediaModel);

  public void onDeliverAccountFiles(ArrayList<AccountMediaModel> accountMediaModelList);

  public void onAccountFolderSelect(String accountName, String accountShortcut,
      String folderId);

  public void onSessionExpired(boolean isSessionExpired, AccountType accountType);

}
