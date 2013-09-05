package com.chute.android.photopickerplus.ui.fragment;

import java.util.ArrayList;

import com.chute.sdk.v2.model.AccountModel;
import com.chute.sdk.v2.model.AssetModel;
import com.chute.sdk.v2.model.enums.AccountType;

public interface AccountFilesListener {

  public void onAccountFilesSelect(AssetModel assetModel);

  public void onDeliverAccountFiles(ArrayList<AssetModel> assetModelList);

  public void onAccountFolderSelect(AccountModel account,
      String folderId);

  public void onSessionExpired(AccountType accountType);

}
