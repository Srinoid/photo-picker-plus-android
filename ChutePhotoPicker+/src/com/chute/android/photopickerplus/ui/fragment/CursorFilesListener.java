package com.chute.android.photopickerplus.ui.fragment;

import java.util.ArrayList;

import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.AssetModel;

public interface CursorFilesListener {

  public void onCursorAssetsSelect(AssetModel assetModel);

  public void onDeliverCursorAssets(ArrayList<String> assetPathList);

}
