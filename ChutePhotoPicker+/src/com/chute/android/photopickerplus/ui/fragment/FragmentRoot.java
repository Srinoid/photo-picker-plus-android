package com.chute.android.photopickerplus.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.loaders.AssetsAsyncTaskLoader;
import com.chute.android.photopickerplus.ui.adapter.AssetCursorAdapter;
import com.chute.android.photopickerplus.ui.adapter.AssetAccountAdapter;
import com.chute.android.photopickerplus.ui.adapter.AssetAccountAdapter.AdapterItemClickListener;
import com.chute.android.photopickerplus.util.AppUtil;
import com.chute.android.photopickerplus.util.NotificationUtil;
import com.chute.android.photopickerplus.util.PhotoFilterType;
import com.chute.sdk.v2.api.accounts.GCAccounts;
import com.chute.sdk.v2.model.AccountAlbumModel;
import com.chute.sdk.v2.model.AccountBaseModel;
import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.response.ResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class FragmentRoot extends Fragment implements AdapterItemClickListener {

  private static final String ARG_FILTER_TYPE = "argFilterType";
  private static final String ARG_ACCOUNT_ID = "argAccountId";
  private static final String ARG_ACCOUNT_NAME = "argAccountName";
  private static final String ARG_ACCOUNT_SHORTCUT = "argAccountShortcut";
  private static final String ARG_MULTIPICKER = "argMultiPicker";
  private static final String ARG_SELECTED_ITEM_POSITIONS = "argSelectedItemPositions";

  private GridView gridView;
  private AssetCursorAdapter cursorAssetAdapter;
  private AssetAccountAdapter accountAssetAdapter;
  private TextView textViewSelectPhotos;
  private View emptyView;

  private boolean isMultipicker;
  private String accountName;
  private String accountShortcut;
  private ArrayList<Integer> selectedItemsPositions;
  private PhotoFilterType filterType;
  private CursorFilesListener cursorListener;
  private AccountFilesListener accountListener;

  public static FragmentRoot newInstance(PhotoFilterType filterType, String accountId,
      boolean isMultiPicker,
      ArrayList<Integer> selectedItemPositions, String accountName, String accountShortcut) {
    FragmentRoot frag = new FragmentRoot();
    Bundle args = new Bundle();
    args.putSerializable(ARG_FILTER_TYPE, filterType);
    args.putString(ARG_ACCOUNT_ID, accountId);
    args.putBoolean(ARG_MULTIPICKER, isMultiPicker);
    args.putIntegerArrayList(ARG_SELECTED_ITEM_POSITIONS, selectedItemPositions);
    args.putString(ARG_ACCOUNT_NAME, accountName);
    args.putString(ARG_ACCOUNT_SHORTCUT, accountShortcut);
    frag.setArguments(args);
    return frag;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    cursorListener = (CursorFilesListener) activity;
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

    Button ok = (Button) view.findViewById(R.id.buttonOk);
    Button cancel = (Button) view.findViewById(R.id.buttonCancel);

    ok.setOnClickListener(new OkClickListener());
    cancel.setOnClickListener(new CancelClickListener());

    if (getArguments() != null && savedInstanceState == null) {
      accountName = getArguments().getString(ARG_ACCOUNT_NAME);
      accountShortcut = getArguments().getString(ARG_ACCOUNT_SHORTCUT);
      updateFragment(getArguments().getString(ARG_ACCOUNT_ID),
          (PhotoFilterType) getArguments().get(ARG_FILTER_TYPE), getArguments()
              .getBoolean(ARG_MULTIPICKER),
          getArguments().getIntegerArrayList(ARG_SELECTED_ITEM_POSITIONS), accountName,
          accountShortcut);
    }

    gridView.setNumColumns(getResources().getInteger(R.integer.grid_columns_assets));

    return view;
  }

  public void updateFragment(String accountId, PhotoFilterType filterType,
      boolean isMultipicker,
      ArrayList<Integer> selectedItemsPositions, String accountName,
      String accountShortcut) {
    this.filterType = filterType;
    this.isMultipicker = isMultipicker;
    this.selectedItemsPositions = selectedItemsPositions;
    this.accountShortcut = accountShortcut;
    this.accountName = accountName;

    if ((filterType == PhotoFilterType.ALL_PHOTOS)
        || (filterType == PhotoFilterType.CAMERA_ROLL)) {
      getActivity().getSupportLoaderManager().initLoader(1, null,
          new AssetsLoaderCallback());
    } else if (filterType == PhotoFilterType.SOCIAL_PHOTOS) {
      GCAccounts.accountRoot(getActivity(), accountName, accountShortcut,
          new RootCallback()).executeAsync();
    }
  }

  private final class RootCallback implements
      HttpCallback<ResponseModel<AccountBaseModel>> {

    @Override
    public void onHttpError(ResponseStatus arg0) {
      NotificationUtil.makeConnectionProblemToast(getActivity().getApplicationContext());
      toggleEmptyViewErrorMessage();

    }

    public void toggleEmptyViewErrorMessage() {
      emptyView.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(ResponseModel<AccountBaseModel> responseData) {
      if (responseData != null && getActivity() != null) {
        accountAssetAdapter = new AssetAccountAdapter(getActivity(),
            responseData.getData(),
            FragmentRoot.this);
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

  /*
   * DEVICE PHOTO LOADER
   */
  private final class AssetsLoaderCallback implements LoaderCallbacks<Cursor> {

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
      return new AssetsAsyncTaskLoader(getActivity().getApplicationContext(), filterType);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
      if (cursor == null) {
        return;
      }
      cursorAssetAdapter = new AssetCursorAdapter(getActivity(), cursor);
      gridView.setAdapter(cursorAssetAdapter);

      if (cursorAssetAdapter.getCount() == 0) {
        emptyView.setVisibility(View.GONE);
      }

      if (selectedItemsPositions != null) {
        for (int position : selectedItemsPositions) {
          cursorAssetAdapter.toggleTick(position);
        }
      }

      if (isMultipicker == true) {
        textViewSelectPhotos.setText(getActivity().getApplicationContext().getResources()
            .getString(R.string.select_photos));
        gridView.setOnItemClickListener(new OnMultiSelectGridItemClickListener());
      } else {
        textViewSelectPhotos.setText(getActivity().getApplicationContext().getResources()
            .getString(R.string.select_a_photo));
        gridView.setOnItemClickListener(new OnSingleSelectGridItemClickListener());
      }
      // NotificationUtil.showPhotosAdapterToast(getActivity().getApplicationContext(),
      // cursorAssetAdapter.getCount());

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
      // TODO Auto-generated method stub

    }

  }

  /*
   * CURSOR ADAPTER CLICK LISTENERS
   */
  private final class OnMultiSelectGridItemClickListener implements OnItemClickListener {

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view,
        final int position, final long id) {
      cursorAssetAdapter.toggleTick(position);
    }
  }

  private final class OnSingleSelectGridItemClickListener implements OnItemClickListener {

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view,
        final int position, final long id) {
      cursorListener.onCursorAssetsSelect(AppUtil.getMediaModel(cursorAssetAdapter
          .getItem(position)));
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
      if (filterType == PhotoFilterType.SOCIAL_PHOTOS) {
        accountListener.onDeliverAccountFiles(accountAssetAdapter.getPhotoCollection());
      } else if ((filterType == PhotoFilterType.ALL_PHOTOS)
          || (filterType == PhotoFilterType.CAMERA_ROLL)) {
        cursorListener.onDeliverCursorAssets(cursorAssetAdapter
            .getSelectedFilePaths());
      }
    }
  }

  @Override
  public void onFolderClicked(int position) {
    AccountAlbumModel album = (AccountAlbumModel) accountAssetAdapter.getItem(position);
    accountListener.onAccountFolderSelect(accountName, accountShortcut,
        album.getId(), isMultipicker);

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

}
