package com.chute.android.photopickerplus.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
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
import com.chute.android.photopickerplus.config.ServiceLoader;
import com.chute.android.photopickerplus.ui.adapter.AssetAccountAdapter;
import com.chute.android.photopickerplus.ui.adapter.AssetAccountAdapter.AdapterItemClickListener;
import com.chute.android.photopickerplus.util.NotificationUtil;
import com.chute.sdk.v2.api.accounts.GCAccounts;
import com.chute.sdk.v2.model.AccountAlbumModel;
import com.chute.sdk.v2.model.AccountBaseModel;
import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.response.ResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class FragmentSingle extends Fragment implements AdapterItemClickListener {

  private static final String ARG_ACCOUNT_TYPE = "argAccountType";
  private static final String ARG_ACCOUNT_SHORTCUT = "argAccountShortcut";
  private static final String ARG_FOLDER_ID = "argAlbumId";
  private static final String ARG_SELECTED_ITEM_POSITIONS = "argSelectedItemPositions";

  private GridView gridView;
  private TextView textViewSelectPhotos;
  private View emptyView;

  private String accountType;
  private String accountShortcut;
  private String folderId;
  private boolean isMultipicker;
  private ArrayList<Integer> selectedItemsPositions;

  private AssetAccountAdapter accountAssetAdapter;
  private AccountFilesListener accountListener;

  public static FragmentSingle newInstance(String accountType, String accountShortcut,
      String folderId, ArrayList<Integer> selectedItemPositions) {
    FragmentSingle frag = new FragmentSingle();
    Bundle args = new Bundle();
    args.putString(ARG_ACCOUNT_TYPE, accountType);
    args.putString(ARG_ACCOUNT_SHORTCUT, accountShortcut);
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
      accountType = getArguments().getString(ARG_ACCOUNT_TYPE);
      accountShortcut = getArguments().getString(ARG_ACCOUNT_SHORTCUT);
      folderId = getArguments().getString(ARG_FOLDER_ID);
      selectedItemsPositions = getArguments().getIntegerArrayList(
          ARG_SELECTED_ITEM_POSITIONS);
      updateFragment(accountType, accountShortcut, folderId,
          selectedItemsPositions);
    }

    Button ok = (Button) view.findViewById(R.id.buttonOk);
    ok.setOnClickListener(new OkClickListener());
    Button cancel = (Button) view.findViewById(R.id.buttonCancel);
    cancel.setOnClickListener(new CancelClickListener());

    gridView.setNumColumns(getResources().getInteger(R.integer.grid_columns_assets));

    return view;
  }

  public void updateFragment(String accountType, String accountShortcut, String folderId,
      ArrayList<Integer> selectedItemsPositions) {
    isMultipicker = ServiceLoader.getInstance().isMultiPicker();
    this.accountType = accountType;
    this.selectedItemsPositions = selectedItemsPositions;
    this.accountShortcut = accountShortcut;
    this.folderId = folderId;

    GCAccounts.accountSingle(getActivity(), accountType, accountShortcut, folderId,
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
      if (responseData != null && getActivity() != null) {
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
        // NotificationUtil.showPhotosAdapterToast(getActivity().getApplicationContext(),
        // accountAssetAdapter.getCount());
      }

    }

  }

  @Override
  public void onFolderClicked(int position) {
    AccountAlbumModel album = (AccountAlbumModel) accountAssetAdapter.getItem(position);
    accountListener.onAccountFolderSelect(accountType, accountShortcut,
        album.getId());

  }

  @Override
  public void onFileClicked(int position) {
    if (isMultipicker == true) {
      accountAssetAdapter.toggleTick(position);
    } else {
      accountListener.onAccountFilesSelect((AccountMediaModel) accountAssetAdapter
          .getItem(position));
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
      accountListener.onDeliverAccountFiles(accountAssetAdapter.getPhotoCollection());
    }
  }

}
