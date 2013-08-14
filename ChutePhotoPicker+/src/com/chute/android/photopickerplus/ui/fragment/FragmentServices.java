package com.chute.android.photopickerplus.ui.fragment;

import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.araneaapps.android.libs.logger.ALog;
import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.dao.MediaDAO;
import com.chute.android.photopickerplus.ui.adapter.ServicesAdapter;
import com.chute.sdk.v2.model.enums.AccountType;

import darko.imagedownloader.ImageLoader;

public class FragmentServices extends Fragment {

	private GridView gridViewServices;
	private ServicesAdapter adapter;

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

	private ImageLoader loader;

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

	public static FragmentServices newInstance(String[] services) {
		FragmentServices frag = new FragmentServices();
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
		loader = ImageLoader.getLoader(getActivity());
		int orientation = getActivity().getResources().getConfiguration().orientation;
//		if (!getActivity().getResources().getBoolean(R.bool.has_two_panes)) {
//			view = inflater.inflate(R.layout.fragment_services_vertical, container, false);
//			gridViewServices = (GridView) view.findViewById(R.id.gridViewServicesVertical);
//			gridViewServices.setOnItemClickListener(new GridClickListener());
//		} else {
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				view = inflater.inflate(R.layout.fragment_services_horizontal, container, false);
				facebook = (ImageView) view.findViewById(R.id.imageViewFacebook);
				facebook.setTag(AccountType.FACEBOOK);
				facebook.setOnClickListener(new OnLoginClickListener());
//				setImageViewDimensions(facebook);
				flickr = (ImageView) view.findViewById(R.id.imageViewFlickr);
				flickr.setTag(AccountType.FLICKR);
				flickr.setOnClickListener(new OnLoginClickListener());
//				setImageViewDimensions(flickr);
				instagram = (ImageView) view.findViewById(R.id.imageViewInstagram);
				instagram.setTag(AccountType.INSTAGRAM);
				instagram.setOnClickListener(new OnLoginClickListener());
//				setImageViewDimensions(instagram);
				picasa = (ImageView) view.findViewById(R.id.imageViewPicasa);
//				picasa.setTag(AccountType.PICASA);
				picasa.setOnClickListener(new OnLoginClickListener());
//				setImageViewDimensions(picasa);
				allPhotos = (ImageView) view.findViewById(R.id.imageViewAllPhotos);
				allPhotos.setOnClickListener(new OnPhotoStreamListener());
//				setImageViewDimensions(allPhotos);
				lastPhotoTaken = (ImageView) view.findViewById(R.id.imageViewLastPhotoTaken);
				lastPhotoTaken.setOnClickListener(new OnLastPhotoClickListener());
//				setImageViewDimensions(lastPhotoTaken);
				takePhoto = (ImageView) view.findViewById(R.id.imageViewTakePhoto);
//				setImageViewDimensions(takePhoto);
				cameraShots = (ImageView) view.findViewById(R.id.imageViewCameraShots);
				cameraShots.setOnClickListener(new OnCameraRollListener());
//				setImageViewDimensions(cameraShots);
			} else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
				view = inflater.inflate(R.layout.fragment_services_vertical, container, false);
				gridViewServices = (GridView) view.findViewById(R.id.gridViewServicesVertical);
				gridViewServices.setOnItemClickListener(new GridClickListener());
			}
//		}
		return view;
	}

	public void configureServices(List<String> servicesArray) {
		ALog.d("Services: " + servicesArray.toString());
		services = new String[servicesArray.size()];
		services = servicesArray.toArray(services);
		adapter = new ServicesAdapter(getActivity(), services);
		int orientation = getActivity().getResources().getConfiguration().orientation;
//		if (!getActivity().getResources().getBoolean(R.bool.has_two_panes)) {
//			gridViewServices.setAdapter(adapter);
//		} else {
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				Uri uriAllPhotos = MediaDAO.getLastPhotoFromAllPhotos(getActivity().getApplicationContext());
				Uri uriLastPhotoFromCameraPhotos = MediaDAO.getLastPhotoFromCameraPhotos(getActivity()
						.getApplicationContext());
				for (String service : servicesArray) {
					if (service.equalsIgnoreCase("Facebook")) {
						facebook.setVisibility(View.VISIBLE);
					}
					if (service.equalsIgnoreCase("Flickr")) {
						flickr.setVisibility(View.VISIBLE);
					}
					if (service.equalsIgnoreCase("Picasa")) {
						picasa.setVisibility(View.VISIBLE);
					}
					if (service.equalsIgnoreCase("Instagram")) {
						instagram.setVisibility(View.VISIBLE);
					}
					if (service.equalsIgnoreCase("Take photo")) {
						takePhoto.setVisibility(View.VISIBLE);
						takePhoto.setOnClickListener(new OnTakePhotoClickListener());
					}
					if (service.equalsIgnoreCase("Last photo taken")) {
						lastPhotoTaken.setVisibility(View.VISIBLE);
						if (uriLastPhotoFromCameraPhotos != null) {
							loader.displayImage(uriLastPhotoFromCameraPhotos.toString(), lastPhotoTaken, null);
						} else {
							lastPhotoTaken.setBackground(getResources().getDrawable(R.drawable.default_thumb));
						}
					}
					if (service.equalsIgnoreCase("Camera shots")) {
						cameraShots.setVisibility(View.VISIBLE);
						if (uriLastPhotoFromCameraPhotos != null) {
							loader.displayImage(uriLastPhotoFromCameraPhotos.toString(), cameraShots, null);
						} else {
							cameraShots.setBackground(getResources().getDrawable(R.drawable.default_thumb));
						}
					}
					if (service.equalsIgnoreCase("All photos")) {
						allPhotos.setVisibility(View.VISIBLE);
						if (uriAllPhotos != null) {
							loader.displayImage(uriAllPhotos.toString(), allPhotos, null);
						} else {
							allPhotos.setBackground(getResources().getDrawable(R.drawable.default_thumb));
						}
					}
				}
			} else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
				gridViewServices.setAdapter(adapter);

//			}
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
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int orientation = getResources().getConfiguration().orientation;
		if (!getResources().getBoolean(R.bool.has_two_panes)) {
//			imageViewThumb.setLayoutParams(new RelativeLayout.LayoutParams(displayMetrics.widthPixels / 3,
//					displayMetrics.widthPixels / 3));
		} else {
			if (orientation == Configuration.ORIENTATION_PORTRAIT) {
				int fragmentHeight = displayMetrics.heightPixels - 310;
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(displayMetrics.widthPixels / 5,
						(int) (fragmentHeight / 1.5));
				params.setMargins(10, 0, 0, 0);
				imageView.setLayoutParams(params);
			} else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//				int fragmentHeight = displayMetrics.heightPixels - 220;
//				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(displayMetrics.widthPixels / 6,
//						(int) (displayMetrics.heightPixels));
//				params.setMargins(10, 0, 0, 0);
//				imageView.setLayoutParams(params);
//				facebook.setScaleType(ScaleType.CENTER_CROP);
			}
		}
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
