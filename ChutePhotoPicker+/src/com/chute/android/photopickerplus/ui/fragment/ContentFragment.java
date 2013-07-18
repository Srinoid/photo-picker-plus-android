package com.chute.android.photopickerplus.ui.fragment;

import java.util.ArrayList;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.loaders.AssetsAsyncTaskLoader;
import com.chute.android.photopickerplus.ui.activity.GridActivity;
import com.chute.android.photopickerplus.ui.adapter.AlbumsAdapter;
import com.chute.android.photopickerplus.ui.adapter.PhotoSelectCursorAdapter;
import com.chute.android.photopickerplus.ui.adapter.PhotosAdapter;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment.ButtonCancelListener;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment.ButtonConfirmCursorAssetsListener;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment.ButtonConfirmSocialAssetsListener;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment.GridCursorSingleSelectListener;
import com.chute.android.photopickerplus.ui.fragment.AssetsFragment.GridSocialSingleSelectListener;
import com.chute.android.photopickerplus.util.AppUtil;
import com.chute.android.photopickerplus.util.ContentType;
import com.chute.android.photopickerplus.util.NotificationUtil;
import com.chute.android.photopickerplus.util.PhotoFilterType;
import com.chute.android.photopickerplus.util.intent.PhotosIntentWrapper;
import com.chute.sdk.v2.api.accounts.GCAccounts;
import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.AccountObjectModel;
import com.chute.sdk.v2.model.response.ListResponseModel;
import com.chute.sdk.v2.utils.PreferenceUtil;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class ContentFragment extends Fragment {

	private TextView textViewContentTitle;
	private View emptyView;
	private LinearLayout linearLayoutButtons;
	private RelativeLayout relativeLayoutContentRoot;
	private Button buttonOk;
	private Button buttonCancel;

	private ListView listViewAlbums;
	private GridView gridViewAssets;
	private AlbumsAdapter albumsAdapter;
	private PhotosAdapter photoSocialAdapter;
	private PhotoSelectCursorAdapter photoCursorAdapter;

	private boolean isMultipicker;
	private PhotoFilterType filterType;
	private String albumID;

	private GridCursorSingleSelectListener gridCursorSelectItemListener;
	private GridSocialSingleSelectListener gridSocialSelectItemListener;
	private ButtonCancelListener cancelListener;
	private ButtonConfirmCursorAssetsListener confirmCursorAssetsListener;
	private ButtonConfirmSocialAssetsListener confirmSocialAssetsListener;
	private AlbumSelectListener albumSelectListener;

	public interface GridCursorSingleSelectListener {
		public void onSelectedCursorItem(AccountMediaModel accountMediaModel, String albumId);
	}

	public interface GridSocialSingleSelectListener {
		public void onSelectedSocialItem(AccountMediaModel accountMediaModel, String albumId);
	}

	public interface ButtonCancelListener {
		public void onCanceled();
	}

	public interface ButtonConfirmSocialAssetsListener {
		public void onOkClicked(ArrayList<AccountMediaModel> accountMediaModelList, String albumId);
	}

	public interface ButtonConfirmCursorAssetsListener {
		public void onOkClicked(ArrayList<String> assetPathList, String albumId);
	}
	
	public interface AlbumSelectListener {
		public void onAlbumSelected();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		gridCursorSelectItemListener = (GridCursorSingleSelectListener) activity;
		gridSocialSelectItemListener = (GridSocialSingleSelectListener) activity;
		cancelListener = (ButtonCancelListener) activity;
		confirmCursorAssetsListener = (ButtonConfirmCursorAssetsListener) activity;
		confirmSocialAssetsListener = (ButtonConfirmSocialAssetsListener) activity;
		albumSelectListener = (AlbumSelectListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_content, container, false);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		relativeLayoutContentRoot = (RelativeLayout) view.findViewById(R.id.relativeLayoutContentRoot);
		textViewContentTitle = (TextView) view.findViewById(R.id.textViewContentTitle);
		emptyView = view.findViewById(R.id.empty_view_layout);
		linearLayoutButtons = (LinearLayout) view.findViewById(R.id.linearLayoutButtons);
		buttonOk = (Button) view.findViewById(R.id.buttonOk);
		buttonCancel = (Button) view.findViewById(R.id.buttonCancel);
	}

	public void updateContent(ContentType contentType, PhotoFilterType photoType, String title, String accountId,
			String albumId, boolean isMultiPicker) {
		this.isMultipicker = isMultiPicker;
		this.albumID = albumId;
		this.filterType = photoType;

		if (contentType.equals(ContentType.ALBUMS)) {
			String albumName = AppUtil.asUpperCaseFirstChar(title.concat(" Albums"));
			textViewContentTitle.setText(albumName);

			initAlbumsListView();

			GCAccounts.albums(getActivity().getApplicationContext(), accountId, new ObjectsCallback()).executeAsync();
		} else {
			// assets
			linearLayoutButtons.setVisibility(View.VISIBLE);
			initPhotoGrid();

			if ((photoType == PhotoFilterType.ALL_PHOTOS) || (photoType == PhotoFilterType.CAMERA_ROLL)) {
				getActivity().getSupportLoaderManager().initLoader(1, null, new AssetsLoaderCallback());
			} else if (photoType == PhotoFilterType.SOCIAL_PHOTOS) {
				GCAccounts.albumMedia(getActivity().getApplicationContext(), accountId, albumId,
						new PhotoListCallback()).executeAsync();
			}

		}

	}

	@SuppressWarnings("deprecation")
	private void initAlbumsListView() {
		listViewAlbums = new ListView(getActivity());
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		relativeParams.setMargins(5, 10, 5, 0);
		relativeParams.addRule(RelativeLayout.BELOW, textViewContentTitle.getId());
		listViewAlbums.setBackgroundDrawable(getResources().getDrawable(R.drawable.services_shape));
		listViewAlbums.setCacheColorHint(Color.TRANSPARENT);
		listViewAlbums.setDivider(getResources().getDrawable(R.drawable.divider_album_list));
		listViewAlbums.setDividerHeight(0);
		listViewAlbums.setScrollbarFadingEnabled(true);
		listViewAlbums.setFadingEdgeLength(0);
		listViewAlbums.setFooterDividersEnabled(false);
		listViewAlbums.setScrollingCacheEnabled(false);

		relativeLayoutContentRoot.addView(listViewAlbums, relativeParams);
	}

	private void initPhotoGrid() {
		gridViewAssets = new GridView(getActivity());
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		relativeParams.addRule(RelativeLayout.BELOW, textViewContentTitle.getId());
		relativeParams.addRule(RelativeLayout.ABOVE, linearLayoutButtons.getId());
		gridViewAssets.setFadingEdgeLength(0);
		gridViewAssets.setFastScrollEnabled(true);
		gridViewAssets.setHorizontalSpacing(0);
		gridViewAssets.setNumColumns(3);
		gridViewAssets.setVerticalSpacing(0);

		relativeLayoutContentRoot.addView(gridViewAssets, relativeParams);

	}

	private final class ObjectsCallback implements HttpCallback<ListResponseModel<AccountObjectModel>> {

		@Override
		public void onSuccess(ListResponseModel<AccountObjectModel> responseData) {
			albumsAdapter = new AlbumsAdapter(getActivity(), (ArrayList<AccountObjectModel>) responseData.getData());
			listViewAlbums.setAdapter(albumsAdapter);
			if (albumsAdapter.getCount() == 0) {
				emptyView.setVisibility(View.GONE);
			}
			// listViewAlbums.setOnItemClickListener(new
			// OnAlbumsClickListener());
			NotificationUtil.showAlbumsAdapterToast(getActivity().getApplicationContext(), albumsAdapter.getCount());
		}

		public void toggleEmptyViewErrorMessage() {
			emptyView.setVisibility(View.GONE);
		}


		@Override
		public void onHttpError(ResponseStatus responseStatus) {
			Log.d("debug",
					"response status = " + responseStatus.getStatusCode() + " " + responseStatus.getStatusMessage());
			NotificationUtil.makeConnectionProblemToast(getActivity().getApplicationContext());
			toggleEmptyViewErrorMessage();

			
		}
	}

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
			photoCursorAdapter = new PhotoSelectCursorAdapter(getActivity(), cursor);
			gridViewAssets.setAdapter(photoCursorAdapter);

			if (photoCursorAdapter.getCount() == 0) {
				emptyView.setVisibility(View.GONE);
			}

			if (isMultipicker == true) {
				textViewContentTitle.setText(getActivity().getApplicationContext().getResources()
						.getString(R.string.select_photos));
				gridViewAssets.setOnItemClickListener(new OnMultiSelectGridItemClickListener());
			} else {
				textViewContentTitle.setText(getActivity().getApplicationContext().getResources()
						.getString(R.string.select_a_photo));
				gridViewAssets.setOnItemClickListener(new OnSingleSelectGridItemClickListener());
			}
			NotificationUtil.showPhotosAdapterToast(getActivity().getApplicationContext(),
					photoCursorAdapter.getCount());

		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			// TODO Auto-generated method stub

		}

	}

	private final class PhotoListCallback implements HttpCallback<ListResponseModel<AccountMediaModel>> {

		@Override
		public void onSuccess(ListResponseModel<AccountMediaModel> responseData) {
			Log.d("debug", "responsedata = " + responseData.getData().toString());
			photoSocialAdapter = new PhotosAdapter(getActivity(), (ArrayList<AccountMediaModel>) responseData.getData());
			gridViewAssets.setAdapter(photoSocialAdapter);

			if (photoSocialAdapter.getCount() == 0) {
				emptyView.setVisibility(View.GONE);
			}

			if (isMultipicker) {
				textViewContentTitle.setText(getResources().getString(R.string.select_photos));
				gridViewAssets.setOnItemClickListener(new OnMultiGridItemClickListener());
			} else {
				textViewContentTitle.setText(getResources().getString(R.string.select_a_photo));
				gridViewAssets.setOnItemClickListener(new OnSingleGridItemClickListener());
			}
			NotificationUtil.showPhotosAdapterToast(getActivity().getApplicationContext(),
					photoSocialAdapter.getCount());
		}

		public void toggleEmptyViewErrorMessage() {
			emptyView.setVisibility(View.GONE);
		}

		@Override
		public void onHttpError(ResponseStatus arg0) {
			NotificationUtil.makeConnectionProblemToast(getActivity().getApplicationContext());
			toggleEmptyViewErrorMessage();

		}
	}

	// CLICK EVENTS
	private final class OnMultiSelectGridItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
			photoCursorAdapter.toggleTick(position);
		}
	}

	private final class OnSingleSelectGridItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
			gridCursorSelectItemListener.onSelectedCursorItem(
					AppUtil.getMediaModel(photoCursorAdapter.getItem(position)), albumID);
		}
	}

	private final class CancelClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			cancelListener.onCanceled();
		}

	}

	private final class OnSingleGridItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
			gridSocialSelectItemListener.onSelectedSocialItem(photoSocialAdapter.getItem(position), albumID);
		}
	}

	private final class OnMultiGridItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
			photoSocialAdapter.toggleTick(position);
		}
	}

	private final class OkClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (filterType == PhotoFilterType.SOCIAL_PHOTOS) {
				confirmSocialAssetsListener.onOkClicked(photoSocialAdapter.getPhotoCollection(), albumID);
			} else if ((filterType == PhotoFilterType.ALL_PHOTOS) || (filterType == PhotoFilterType.CAMERA_ROLL)) {
				confirmCursorAssetsListener.onOkClicked(photoCursorAdapter.getSelectedFilePaths(), albumID);
			}
		}
	}
}
