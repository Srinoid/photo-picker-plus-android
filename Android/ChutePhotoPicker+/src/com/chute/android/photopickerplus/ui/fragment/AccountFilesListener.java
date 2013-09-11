/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.ui.fragment;

import java.util.ArrayList;

import com.chute.android.photopickerplus.ui.activity.AssetActivity;
import com.chute.android.photopickerplus.ui.activity.ServicesActivity;
import com.chute.sdk.v2.model.AccountModel;
import com.chute.sdk.v2.model.AssetModel;
import com.chute.sdk.v2.model.enums.AccountType;

/**
 * 
 * This interface is implemented by {@link AssetActivity} and
 * {@link ServicesActivity}.
 * 
 */
public interface AccountFilesListener {

  /**
   * Delivers {@link AssetModel} to the main activity when a photo from a remote
   * service is selected.
   * 
   * @param assetModel
   *          The {@link AssetModel} delivered to the main activity i.e. the
   *          activity that started the PhotoPicker.
   */
  public void onAccountFilesSelect(AssetModel assetModel);

  /**
   * Delivers a list of {@link AssetModel}s to the main activity when photos
   * from a remote service are selected.
   * 
   * @param assetModelList
   *          {@link AssetModel} list delivered to the main activity i.e. the
   *          activity that started the PhotoPicker.
   */
  public void onDeliverAccountFiles(ArrayList<AssetModel> assetModelList);

  /**
   * Triggered when a remote service folder or album is selected.
   * 
   * @param account
   *          {@link AccountModel} the album belongs to.
   * @param folderId
   *          The ID of the album/folder.
   */
  public void onAccountFolderSelect(AccountModel account,
      String folderId);

  /**
   * This method is used to handle expired session events. It is triggered when
   * HTTP Error containing 401 error code is thrown. It happens when the account
   * is inactive for 15 min. Google and SkyDrive usually manifest this kind of a
   * behavior.
   * 
   * @param accountType
   *          The account whose session is expired.
   */
  public void onSessionExpired(AccountType accountType);

}
