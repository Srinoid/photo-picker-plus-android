package com.chute.android.photopickerplus.ui.fragment;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.araneaapps.android.libs.logger.ALog;
import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.ui.adapter.ServicesVerticalGridAdapter;

public class FragmentServicesVerticalGrid extends Fragment {


	private GridView gridViewServices;
	private ServicesVerticalGridAdapter adapter;



	public static FragmentServicesVerticalGrid newInstance(String[] services) {
		FragmentServicesVerticalGrid frag = new FragmentServicesVerticalGrid();
		Bundle args = new Bundle();
		frag.setArguments(args);
		return frag;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_services_vertical_grid, container, false);

		gridViewServices = (GridView) view.findViewById(R.id.gridViewServicesVertical);
		return view;
	}
	
	public void configureServices(List<String> servicesArray) {
		ALog.d("Services: " + servicesArray.toString());
		String[] services = new String[servicesArray.size()];
	    services = servicesArray.toArray(services);
	    adapter = new ServicesVerticalGridAdapter(getActivity(), services);
		gridViewServices.setAdapter(adapter);

	}
	
	



}
