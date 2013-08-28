package com.chute.android.photopickerplus.ui.fragment;

import java.util.ArrayList;

import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.AccountModel;
import com.chute.sdk.v2.model.enums.AccountType;

public interface AccountFilesListener {

  public void onAccountFilesSelect(AccountMediaModel accountMediaModel);

  public void onDeliverAccountFiles(ArrayList<AccountMediaModel> accountMediaModelList);

  public void onAccountFolderSelect(AccountModel account,
      String folderId);

  public void onSessionExpired(AccountType accountType);

}
