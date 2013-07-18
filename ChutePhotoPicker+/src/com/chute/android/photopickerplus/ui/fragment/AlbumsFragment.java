package com.chute.android.photopickerplus.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.ui.adapter.AlbumsAdapter;
import com.chute.android.photopickerplus.util.AppUtil;
import com.chute.android.photopickerplus.util.NotificationUtil;
import com.chute.sdk.v2.api.accounts.GCAccounts;
import com.chute.sdk.v2.model.AccountObjectModel;
import com.chute.sdk.v2.model.response.ListResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class AlbumsFragment extends Fragment {

	private static final String ARG_ALBUM_TITLE = "argAlbumTitle";
	private static final String ARG_ACCOUNT_ID = "argAccountId";
	private ListView listViewAlbums;
	private AlbumsAdapter adapter;
	private View emptyView;
	private SelectAlbumListener albumListener;
	private TextView textViewAlbumTitle;

	public interface SelectAlbumListener {
		public void onAlbumSelected(AccountObjectModel model);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		albumListener = (SelectAlbumListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_albums, container, false);

		listViewAlbums = (ListView) view.findViewById(R.id.listViewAlbums);
		emptyView = view.findViewById(R.id.empty_view_layout);
		listViewAlbums.setEmptyView(emptyView);

		textViewAlbumTitle = (TextView) view.findViewById(R.id.textViewAlbumTitle);

		return view;
	}

	public void updateFragment(String accountTitle, String accountId) {
		String albumName = AppUtil.asUpperCaseFirstChar(accountTitle.concat(" Albums"));
		textViewAlbumTitle.setText(albumName);

		GCAccounts.albums(getActivity().getApplicationContext(), accountId, new ObjectsCallback()).executeAsync();
	}

	private final class ObjectsCallback implements HttpCallback<ListResponseModel<AccountObjectModel>> {

		@Override
		public void onSuccess(ListResponseModel<AccountObjectModel> responseData) {
			adapter = new AlbumsAdapter(getActivity(), (ArrayList<AccountObjectModel>) responseData.getData());
			listViewAlbums.setAdapter(adapter);
			if (adapter.getCount() == 0) {
				emptyView.setVisibility(View.GONE);
			}
			listViewAlbums.setOnItemClickListener(new OnAlbumsClickListener());
			NotificationUtil.showAlbumsAdapterToast(getActivity().getApplicationContext(), adapter.getCount());
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

	private final class OnAlbumsClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			AccountObjectModel accountObjectModel = adapter.getItem(position);
			albumListener.onAlbumSelected(accountObjectModel);
		}

	}

}
