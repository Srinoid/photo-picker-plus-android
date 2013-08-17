package com.chute.android.photopickerplus.ui.fragment;

import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.araneaapps.android.libs.logger.ALog;
import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.ui.adapter.ServicesAdapter;
import com.chute.sdk.v2.model.enums.AccountType;
import com.chute.sdk.v2.model.enums.LocalMediaType;

public class FragmentServices extends Fragment {

	private final int GRID_COLUMNS_LANDSCAPE_ONEPANE = 5;
	private final int GRID_COLUMNS_LANDSCAPE_TWOPANES = 8;
	private GridView gridViewServices;
	private ServicesAdapter adapter;

	private ServiceClickedListener serviceClickedListener;

	private AccountType[] services;

	public interface ServiceClickedListener {
		public void accountLogin(AccountType accountType);

		public void photoStream();

		public void cameraRoll();

		public void lastPhoto();

		public void takePhoto();

	}

	public static FragmentServices newInstance(String[] services) {
		FragmentServices frag = new FragmentServices();
		Bundle args = new Bundle();
		frag.setArguments(args);
		return frag;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		serviceClickedListener = (ServiceClickedListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		view = inflater.inflate(R.layout.fragment_services, container, false);
		gridViewServices = (GridView) view.findViewById(R.id.gridViewServicesVertical);
		gridViewServices.setOnItemClickListener(new GridClickListener());
		int orientation = getActivity().getResources().getConfiguration().orientation;
		if (!getActivity().getResources().getBoolean(R.bool.has_two_panes)) {
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				gridViewServices.setNumColumns(GRID_COLUMNS_LANDSCAPE_ONEPANE);
			}
		} else {
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				gridViewServices.setNumColumns(GRID_COLUMNS_LANDSCAPE_TWOPANES);
			}
		}
		return view;
	}

	public void configureServices(List<AccountType> servicesArray) {
		ALog.d("Services: " + servicesArray.toString());
		services = new AccountType[servicesArray.size()];
		services = servicesArray.toArray(services);
		adapter = new ServicesAdapter(getActivity(), services);
		gridViewServices.setAdapter(adapter);

	}

	private final class GridClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			AccountType service = services[position];
			if (service.equals(AccountType.FACEBOOK)) {
				serviceClickedListener.accountLogin(AccountType.FACEBOOK);
			}
			if (service.equals(AccountType.FLICKR)) {
				serviceClickedListener.accountLogin(AccountType.FLICKR);
			}
			if (service.equals(AccountType.PICASA)) {
				serviceClickedListener.accountLogin(AccountType.PICASA);
			}
			if (service.equals(AccountType.INSTAGRAM)) {
				serviceClickedListener.accountLogin(AccountType.INSTAGRAM);
			}
//			if (service.equalsIgnoreCase(LocalMediaType.ALL_PHOTOS.getName())) {
//				serviceClickedListener.photoStream();
//			}
//			if (service.equalsIgnoreCase(LocalMediaType.CAMERA_SHOTS.getName())) {
//				serviceClickedListener.cameraRoll();
//			}
//			if (service.equalsIgnoreCase(LocalMediaType.TAKE_PHOTO.getName())) {
//				serviceClickedListener.takePhoto();
//			}
//			if (service.equalsIgnoreCase(LocalMediaType.LAST_PHOTO_TAKEN.getName())) {
//				serviceClickedListener.lastPhoto();
//			}
		}

	}

}
