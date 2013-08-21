package com.chute.android.photopickerplus.ui.fragment;

import java.util.ArrayList;

import com.chute.sdk.v2.model.AccountMediaModel;

public interface AccountFilesListener {

  public void onAccountFilesSelect(AccountMediaModel accountMediaModel);

  public void onDeliverAccountFiles(ArrayList<AccountMediaModel> accountMediaModelList);

  public void onAccountFolderSelect(String accountType, String accountShortcut,
      String folderId, boolean isMultipicker);

}
