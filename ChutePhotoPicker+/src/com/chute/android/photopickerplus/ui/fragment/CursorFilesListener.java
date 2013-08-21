package com.chute.android.photopickerplus.ui.fragment;

import java.util.ArrayList;

import com.chute.sdk.v2.model.AccountMediaModel;

public interface CursorFilesListener {

  public void onCursorAssetsSelect(AccountMediaModel accountMediaModel);

  public void onDeliverCursorAssets(ArrayList<String> assetPathList);

}
