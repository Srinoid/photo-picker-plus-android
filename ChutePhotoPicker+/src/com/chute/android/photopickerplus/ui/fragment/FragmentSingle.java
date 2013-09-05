package com.chute.android.photopickerplus.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.araneaapps.android.libs.logger.ALog;
import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.callback.ImageDataResponseLoader;
import com.chute.android.photopickerplus.config.PhotoPicker;
import com.chute.android.photopickerplus.ui.adapter.AssetAccountAdapter;
import com.chute.android.photopickerplus.ui.adapter.AssetAccountAdapter.AdapterItemClickListener;
import com.chute.android.photopickerplus.util.NotificationUtil;
import com.chute.android.photopickerplus.util.PhotoPickerPreferenceUtil;
import com.chute.sdk.v2.api.Chute;
import com.chute.sdk.v2.api.accounts.GCAccounts;
import com.chute.sdk.v2.api.authentication.AuthConstants;
import com.chute.sdk.v2.api.authentication.TokenAuthenticationProvider;
import com.chute.sdk.v2.model.AccountAlbumModel;
import com.chute.sdk.v2.model.AccountBaseModel;
import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.AccountModel;
import com.chute.sdk.v2.model.response.ResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class FragmentSingle extends Fragment implements AdapterItemClickListener {

  private static final String ARG_ACCOUNT_MODEL = "argAccountModel";
  private static final String ARG_FOLDER_ID = "argAlbumId";
  private static final String ARG_SELECTED_ITEM_POSITIONS = "argSelectedItemPositions";

  private GridView gridView;
  private TextView textViewSelectPhotos;
  private View emptyView;

  private AccountModel account;
  private String folderId;
  private boolean isMultipicker;
  private ArrayList<Integer> selectedItemsPositions;

  private AssetAccountAdapter accountAssetAdapter;
  private AccountFilesListener accountListener;

  public static FragmentSingle newInstance(AccountModel account,
      String folderId, ArrayList<Integer> selectedItemPositions) {
    FragmentSingle frag = new FragmentSingle();
    Bundle args = new Bundle();
    args.putParcelable(ARG_ACCOUNT_MODEL, account);
    args.putString(ARG_FOLDER_ID, folderId);
    args.putIntegerArrayList(ARG_SELECTED_ITEM_POSITIONS, selectedItemPositions);
    frag.setArguments(args);
    return frag;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    accountListener = (AccountFilesListener) activity;

  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    setRetainInstance(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_assets, container, false);

    textViewSelectPhotos = (TextView) view.findViewById(R.id.textViewSelectPhotos);
    gridView = (GridView) view.findViewById(R.id.gridViewAssets);
    emptyView = view.findViewById(R.id.empty_view_layout);
    gridView.setEmptyView(emptyView);

    if (getArguments() != null) {
      account = getArguments().getParcelable(ARG_ACCOUNT_MODEL);
      folderId = getArguments().getString(ARG_FOLDER_ID);
      selectedItemsPositions = getArguments().getIntegerArrayList(
          ARG_SELECTED_ITEM_POSITIONS);
      updateFragment(account, folderId, selectedItemsPositions);
    }

    Button ok = (Button) view.findViewById(R.id.buttonOk);
    ok.setOnClickListener(new OkClickListener());
    Button cancel = (Button) view.findViewById(R.id.buttonCancel);
    cancel.setOnClickListener(new CancelClickListener());

    gridView.setNumColumns(getResources().getInteger(R.integer.grid_columns_assets));

    return view;
  }

  public void updateFragment(AccountModel account, String folderId,
      ArrayList<Integer> selectedItemsPositions) {
    
    isMultipicker = PhotoPicker.getInstance().isMultiPicker();
    this.account = account;
    this.selectedItemsPositions = selectedItemsPositions;
    this.folderId = folderId;

    String encodedId = Uri.encode(folderId);
    GCAccounts.accountSingle(getActivity(),
        PhotoPickerPreferenceUtil.get().getAccountType().name().toLowerCase(),
        account.getShortcut(), encodedId,
        new AccountSingleCallback()).executeAsync();

  }

  private final class AccountSingleCallback implements
      HttpCallback<ResponseModel<AccountBaseModel>> {

    @Override
    public void onHttpError(ResponseStatus responseStatus) {
      ALog.d("Http Error: " + responseStatus.getStatusMessage() + " "
          + responseStatus.getStatusCode());
      NotificationUtil.makeConnectionProblemToast(getActivity());

    }

    @Override
    public void onSuccess(ResponseModel<AccountBaseModel> responseData) {
      if (responseData.getData() != null && getActivity() != null) {
        accountAssetAdapter = new AssetAccountAdapter(getActivity(),
            responseData.getData(),
            FragmentSingle.this);
        gridView.setAdapter(accountAssetAdapter);

        if (accountAssetAdapter.getCount() == 0) {
          emptyView.setVisibility(View.GONE);
        }

        if (selectedItemsPositions != null) {
          for (int position : selectedItemsPositions) {
            accountAssetAdapter.toggleTick(position);
          }
        }

        if (isMultipicker == true) {
          textViewSelectPhotos.setText(getActivity().getApplicationContext()
              .getResources()
              .getString(R.string.select_photos));
        } else {
          textViewSelectPhotos.setText(getActivity().getApplicationContext()
              .getResources()
              .getString(R.string.select_a_photo));
        }
        NotificationUtil.showPhotosAdapterToast(getActivity().getApplicationContext(),
            accountAssetAdapter.getCount());

      }

    }

  }

  @Override
  public void onFolderClicked(int position) {
    AccountAlbumModel album = (AccountAlbumModel) accountAssetAdapter.getItem(position);
    accountListener.onAccountFolderSelect(account,
        album.getId());

  }

  @Override
  public void onFileClicked(int position) {
    if (isMultipicker == true) {
      accountAssetAdapter.toggleTick(position);
    } else {
       ArrayList<AccountMediaModel> accountMediaModelList = new
       ArrayList<AccountMediaModel>();
       accountMediaModelList.add((AccountMediaModel) accountAssetAdapter
       .getItem(position));
       ImageDataResponseLoader.postImageData(getActivity().getApplicationContext(),
       accountMediaModelList, accountListener);
//      accountListener.onAccountFilesSelect((AccountMediaModel) accountAssetAdapter
//          .getItem(position));
    }

  }

  private final class CancelClickListener implements OnClickListener {

    @Override
    public void onClick(View v) {
      getActivity().finish();
    }

  }

  private final class OkClickListener implements OnClickListener {

    @Override
    public void onClick(View v) {
      ImageDataResponseLoader.postImageData(getActivity().getApplicationContext(),
          accountAssetAdapter.getPhotoCollection(), accountListener);
//      accountListener.onDeliverAccountFiles(accountAssetAdapter.getPhotoCollection());
    }
  }

}