package com.chute.android.photopickerplus.ui.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.dao.MediaDAO;
import com.chute.sdk.v2.model.enums.AccountType;

import darko.imagedownloader.ImageLoader;

public class ChooseServiceFragment extends Fragment {

	private static final String ARG_HIDDEN_SERVICES = "argHiddenServices";
	
	private TextView textViewFacebook;
	private TextView textViewPicasa;
	private TextView textViewFlickr;
	private TextView textViewInstagram;
	private TextView textViewUserTitle;
	private LinearLayout linearLayoutServices;
	private LinearLayout linearLayoutTakePhoto;
	private LinearLayout linearLayoutFacebook;
	private LinearLayout linearLayoutPicasa;
	private LinearLayout linearLayoutInstagram;
	private LinearLayout linearLayoutFlickr;
	private LinearLayout linearLayoutAllPhotos;
	private LinearLayout linearLayoutCameraPhotos;
	private LinearLayout linearLayoutLastPhoto;
	private ImageView imageViewAllPhotos;
	private ImageView imageViewCameraPhotos;
	private ImageView imageViewLastPhoto;
	private AccountType accountType;
	private ImageLoader imageLoader;

	// private String token;
	private boolean areServicesHidden;

	private LoginListener loginListener;
	private PhotoStreamListener photoStreamListener;
	private CameraRollListener cameraRollListener;
	private LastPhotoListener lastPhotoListener;
	private TakePhotoListener takePhotoListener;

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

	public static ChooseServiceFragment newInstance(boolean areServicesHidden) {
		ChooseServiceFragment frag = new ChooseServiceFragment();
		Bundle args = new Bundle();
		args.putBoolean(ARG_HIDDEN_SERVICES, areServicesHidden);
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
		View view = inflater.inflate(R.layout.fragment_services, container, false);
		imageLoader = ImageLoader.getLoader(getActivity());
		
//		areServicesHidden = getArguments().getBoolean(ARG_HIDDEN_SERVICES);

		initViews(view);

		imageLoader.displayImage(MediaDAO.getLastPhotoFromAllPhotos(getActivity().getApplicationContext()).toString(),
				imageViewAllPhotos, null);
		Uri uri = MediaDAO.getLastPhotoFromCameraPhotos(getActivity().getApplicationContext());
		if (uri != null) {
			imageLoader.displayImage(uri.toString(), imageViewCameraPhotos, null);
			imageLoader.displayImage(uri.toString(), imageViewLastPhoto, null);
		}

		linearLayoutFacebook.setOnClickListener(new OnLoginClickListener());
		linearLayoutPicasa.setOnClickListener(new OnLoginClickListener());
		linearLayoutFlickr.setOnClickListener(new OnLoginClickListener());
		linearLayoutInstagram.setOnClickListener(new OnLoginClickListener());
		linearLayoutAllPhotos.setOnClickListener(new OnPhotoStreamListener());
		linearLayoutCameraPhotos.setOnClickListener(new OnCameraRollListener());
		linearLayoutLastPhoto.setOnClickListener(new OnLastPhotoClickListener());
		linearLayoutTakePhoto.setOnClickListener(new OnTakePhotoClickListener());
		
//		if (areServicesHidden) {
//			linearLayoutServices.setVisibility(View.GONE);
//			textViewUserTitle.setVisibility(View.GONE);
//		}
		return view;
	}

	public void initViews(View view) {
		textViewFacebook = (TextView) view.findViewById(R.id.textViewFacebook);
		textViewFacebook.setTag(AccountType.FACEBOOK);
		textViewPicasa = (TextView) view.findViewById(R.id.textViewPicasa);
		textViewPicasa.setTag(AccountType.PICASA);
		textViewFlickr = (TextView) view.findViewById(R.id.textViewFlickr);
		textViewFlickr.setTag(AccountType.FLICKR);
		textViewInstagram = (TextView) view.findViewById(R.id.textViewInstagram);
		textViewInstagram.setTag(AccountType.INSTAGRAM);
		textViewUserTitle = (TextView) view.findViewById(R.id.textViewUserTitle);

		linearLayoutServices = (LinearLayout) view.findViewById(R.id.linearLayoutSocialServicesContent);
		linearLayoutAllPhotos = (LinearLayout) view.findViewById(R.id.linearLayoutAllPhotos);
		linearLayoutCameraPhotos = (LinearLayout) view.findViewById(R.id.linearLayoutCameraShots);
		linearLayoutLastPhoto = (LinearLayout) view.findViewById(R.id.linearLayoutLastPhotoTaken);
		linearLayoutTakePhoto = (LinearLayout) view.findViewById(R.id.linearLayoutTakePhoto);

		linearLayoutFacebook = (LinearLayout) view.findViewById(R.id.linearLayoutFacebook);
		linearLayoutFacebook.setTag(AccountType.FACEBOOK);
		linearLayoutFlickr = (LinearLayout) view.findViewById(R.id.linearLayoutFlickr);
		linearLayoutFlickr.setTag(AccountType.FLICKR);
		linearLayoutPicasa = (LinearLayout) view.findViewById(R.id.linearLayoutPicasa);
		linearLayoutPicasa.setTag(AccountType.PICASA);
		linearLayoutInstagram = (LinearLayout) view.findViewById(R.id.linearLayoutInstagram);
		linearLayoutInstagram.setTag(AccountType.INSTAGRAM);

		imageViewAllPhotos = (ImageView) view.findViewById(R.id.imageViewAllPhotos);
		imageViewCameraPhotos = (ImageView) view.findViewById(R.id.imageViewCameraShots);
		imageViewLastPhoto = (ImageView) view.findViewById(R.id.imageViewLastPhotoTaken);

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
