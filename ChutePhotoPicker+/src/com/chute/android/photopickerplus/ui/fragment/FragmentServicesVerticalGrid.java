package com.chute.android.photopickerplus.ui.fragment;

import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.araneaapps.android.libs.logger.ALog;
import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.ui.adapter.ServicesVerticalGridAdapter;
import com.chute.sdk.v2.model.enums.AccountType;

public class FragmentServicesVerticalGrid extends Fragment {

	private GridView gridViewServices;
	private ServicesVerticalGridAdapter adapter;

	private ImageView facebook;
	private ImageView flickr;
	private ImageView instagram;
	private ImageView picasa;

	private ImageView takePhoto;
	private ImageView lastPhotoTaken;
	private ImageView cameraShots;
	private ImageView allPhotos;

	private LoginListener loginListener;
	private PhotoStreamListener photoStreamListener;
	private CameraRollListener cameraRollListener;
	private LastPhotoListener lastPhotoListener;
	private TakePhotoListener takePhotoListener;

	private AccountType accountType;
	private String[] services;

	public interface LoginListener {
		public void accountLogin(AccountType accountType);
	}

	public interface PhotoStreamListener {
		public void photoStream();
	}

	public interface CameraRollListener {
		public void cameraRoll();
	}

	public interface LastPhotoListener {
		public void lastPhoto();
	}

	public interface TakePhotoListener {
		public void takePhoto();
	}

	public static FragmentServicesVerticalGrid newInstance(String[] services) {
		FragmentServicesVerticalGrid frag = new FragmentServicesVerticalGrid();
		Bundle args = new Bundle();
		frag.setArguments(args);
		return frag;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		loginListener = (LoginListener) activity;
		photoStreamListener = (PhotoStreamListener) activity;
		cameraRollListener = (CameraRollListener) activity;
		lastPhotoListener = (LastPhotoListener) activity;
		takePhotoListener = (TakePhotoListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		int orientation = getActivity().getResources().getConfiguration().orientation;
		if (!getActivity().getResources().getBoolean(R.bool.has_two_panes)) {
			// one pane
		} else {
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				view = inflater.inflate(R.layout.fragment_services_horizontal_grid2, container, false);
				facebook = (ImageView) view.findViewById(R.id.imageViewFacebook);
				setImageViewDimensions(facebook);
				flickr = (ImageView) view.findViewById(R.id.imageViewFlickr);
				setImageViewDimensions(flickr);
				instagram = (ImageView) view.findViewById(R.id.imageViewInstagram);
				setImageViewDimensions(instagram);
				picasa = (ImageView) view.findViewById(R.id.imageViewPicasa);
				setImageViewDimensions(picasa);
				allPhotos = (ImageView) view.findViewById(R.id.imageViewAllPhotos);
				setImageViewDimensions(allPhotos);
				lastPhotoTaken = (ImageView) view.findViewById(R.id.imageViewLastPhotoTaken);
				setImageViewDimensions(lastPhotoTaken);
				takePhoto = (ImageView) view.findViewById(R.id.imageViewTakePhoto);
				setImageViewDimensions(takePhoto);
				cameraShots = (ImageView) view.findViewById(R.id.imageViewCameraShots);
				setImageViewDimensions(cameraShots);
			} else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
				view = inflater.inflate(R.layout.fragment_services_vertical_grid, container, false);
				gridViewServices = (GridView) view.findViewById(R.id.gridViewServicesVertical);
				gridViewServices.setOnItemClickListener(new GridClickListener());
			}
		}
		return view;
	}

	public void configureServices(List<String> servicesArray) {
		ALog.d("Services: " + servicesArray.toString());
		services = new String[servicesArray.size()];
		services = servicesArray.toArray(services);
		adapter = new ServicesVerticalGridAdapter(getActivity(), services);
		int orientation = getActivity().getResources().getConfiguration().orientation;
		if (!getActivity().getResources().getBoolean(R.bool.has_two_panes)) {
			// one pane
		} else {
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				for (String service : servicesArray) {
					if (service.equalsIgnoreCase("Facebook")) {
						facebook.setVisibility(View.VISIBLE);
						facebook.setTag(AccountType.FACEBOOK);
						facebook.setOnClickListener(new OnLoginClickListener());
					}
					if (service.equalsIgnoreCase("Flickr")) {
						flickr.setVisibility(View.VISIBLE);
						flickr.setTag(AccountType.FLICKR);
						flickr.setOnClickListener(new OnLoginClickListener());
					}
					if (service.equalsIgnoreCase("Picasa")) {
						picasa.setVisibility(View.VISIBLE);
						picasa.setTag(AccountType.PICASA);
						picasa.setOnClickListener(new OnLoginClickListener());
					}
					if (service.equalsIgnoreCase("Instagram")) {
						instagram.setVisibility(View.VISIBLE);
						instagram.setTag(AccountType.INSTAGRAM);
						instagram.setOnClickListener(new OnLoginClickListener());
					}
					if (service.equalsIgnoreCase("Take photo")) {
						takePhoto.setVisibility(View.VISIBLE);
						takePhoto.setOnClickListener(new OnTakePhotoClickListener());
					}
					if (service.equalsIgnoreCase("Last photo taken")) {
						lastPhotoTaken.setVisibility(View.VISIBLE);
						lastPhotoTaken.setOnClickListener(new OnLastPhotoClickListener());
					}
					if (service.equalsIgnoreCase("Camera shots")) {
						cameraShots.setVisibility(View.VISIBLE);
						cameraShots.setOnClickListener(new OnCameraRollListener());
					}
					if (service.equalsIgnoreCase("All photos")) {
						allPhotos.setVisibility(View.VISIBLE);
						allPhotos.setOnClickListener(new OnPhotoStreamListener());
					}
				}
			} else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
				gridViewServices.setAdapter(adapter);
				
			}
		}

	}
	
	private final class GridClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String service = services[position];
			if (service.equalsIgnoreCase("facebook")) {
				loginListener.accountLogin(AccountType.FACEBOOK);
			}
			if (service.equalsIgnoreCase("flickr")) {
				loginListener.accountLogin(AccountType.FLICKR);
			}
			if (service.equalsIgnoreCase("picasa")) {
				loginListener.accountLogin(AccountType.PICASA);
			}
			if (service.equalsIgnoreCase("instagram")) {
				loginListener.accountLogin(AccountType.INSTAGRAM);
			}
			if (service.equalsIgnoreCase("All photos")) {
				photoStreamListener.photoStream();
			}
			if (service.equalsIgnoreCase("Camera shots")) {
				cameraRollListener.cameraRoll();
			}
			if (service.equalsIgnoreCase("Take photo")) {
				takePhotoListener.takePhoto();
			}
			if (service.equalsIgnoreCase("Last photo taken")) {
				lastPhotoListener.lastPhoto();
			}
		}
		
	}

	private void setImageViewDimensions(ImageView imageView) {
		DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
		int fragmentHeight = displayMetrics.heightPixels - 310;
		imageView.setLayoutParams(new LinearLayout.LayoutParams(displayMetrics.widthPixels / 5,
				(int) (fragmentHeight / 1.5)));
	}

	private final class OnLoginClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			accountType = (AccountType) v.getTag();
			loginListener.accountLogin(accountType);
		}

	}

	private final class OnPhotoStreamListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			photoStreamListener.photoStream();
		}
	}

	private final class OnCameraRollListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			cameraRollListener.cameraRoll();
		}

	}

	private final class OnLastPhotoClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			lastPhotoListener.lastPhoto();
		}

	}

	private final class OnTakePhotoClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			takePhotoListener.takePhoto();
		}

	}

}
