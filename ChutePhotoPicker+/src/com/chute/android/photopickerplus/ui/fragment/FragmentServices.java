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

public class FragmentServices extends Fragment {

	private GridView gridViewServices;
	private ServicesAdapter adapter;

	private ServiceClickedListener serviceClickedListener;

	private String[] services;

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
				gridViewServices.setNumColumns(5);
			}
		} else {
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				gridViewServices.setNumColumns(8);
			}
		}
		return view;
	}

	public void configureServices(List<String> servicesArray) {
		ALog.d("Services: " + servicesArray.toString());
		services = new String[servicesArray.size()];
		services = servicesArray.toArray(services);
		adapter = new ServicesAdapter(getActivity(), services);
		gridViewServices.setAdapter(adapter);

	}

	private final class GridClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String service = services[position];
			if (service.equalsIgnoreCase(AccountType.FACEBOOK.name())) {
				serviceClickedListener.accountLogin(AccountType.FACEBOOK);
			}
			if (service.equalsIgnoreCase(AccountType.FLICKR.name())) {
				serviceClickedListener.accountLogin(AccountType.FLICKR);
			}
			if (service.equalsIgnoreCase(AccountType.PICASA.name())) {
				serviceClickedListener.accountLogin(AccountType.PICASA);
			}
			if (service.equalsIgnoreCase(AccountType.INSTAGRAM.name())) {
				serviceClickedListener.accountLogin(AccountType.INSTAGRAM);
			}
			if (service.equalsIgnoreCase(AccountType.ALL_PHOTOS.name())) {
				serviceClickedListener.photoStream();
			}
			if (service.equalsIgnoreCase(AccountType.CAMERA_SHOTS.name())) {
				serviceClickedListener.cameraRoll();
			}
			if (service.equalsIgnoreCase(AccountType.TAKE_PHOTO.name())) {
				serviceClickedListener.takePhoto();
			}
			if (service.equalsIgnoreCase(AccountType.LAST_PHOTO_TAKEN.name())) {
				serviceClickedListener.lastPhoto();
			}
		}

	}

}
