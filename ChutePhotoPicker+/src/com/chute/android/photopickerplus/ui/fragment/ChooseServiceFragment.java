package com.chute.android.photopickerplus.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.config.ConfigurableServices;
import com.chute.android.photopickerplus.dao.MediaDAO;
import com.chute.sdk.v2.model.enums.AccountType;

import darko.imagedownloader.ImageLoader;

public class ChooseServiceFragment extends Fragment {

	private TextView textViewFacebook;
	private TextView textViewPicasa;
	private TextView textViewFlickr;
	private TextView textViewInstagram;
	private TextView textViewUserTitle;
	private TextView textViewDefaultTitle;
	private LinearLayout linearLayoutServices;
	private LinearLayout linearLayoutTakePhoto;
	private LinearLayout linearLayoutFacebook;
	private LinearLayout linearLayoutPicasa;
	private LinearLayout linearLayoutInstagram;
	private LinearLayout linearLayoutFlickr;
	private LinearLayout linearLayoutAllPhotos;
	private LinearLayout linearLayoutCameraPhotos;
	private LinearLayout linearLayoutLastPhoto;
	private ImageView dividerFacebook;
	private ImageView dividerFlicker;
	private ImageView dividerPicasa;
	private ImageView dividerInstagram;
	private ImageView imageViewAllPhotos;
	private ImageView imageViewCameraPhotos;
	private ImageView imageViewLastPhoto;
	private AccountType accountType;
	private ImageLoader imageLoader;

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

	public static ChooseServiceFragment newInstance() {
		ChooseServiceFragment frag = new ChooseServiceFragment();
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
		textViewDefaultTitle = (TextView) view.findViewById(R.id.textViewDefaultTitle);

		dividerFacebook = (ImageView) view.findViewById(R.id.dividerFacebook);
		dividerPicasa = (ImageView) view.findViewById(R.id.dividerPicasa);
		dividerFlicker = (ImageView) view.findViewById(R.id.dividerFlickr);
		dividerInstagram = (ImageView) view.findViewById(R.id.dividerInstagram);

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

	public void setUserName(AccountType accountType, String username) {
		if (accountType == AccountType.FACEBOOK) {
			textViewFacebook.setText(username);
		}
		if (accountType == AccountType.PICASA) {
			textViewPicasa.setText(username);
		}
		if (accountType == AccountType.FLICKR) {
			textViewFlickr.setText(username);
		}
		if (accountType == AccountType.INSTAGRAM) {
			textViewInstagram.setText(username);
		}
	}

	public void setSocialServicesHidden() {
		linearLayoutServices.setVisibility(View.GONE);
		textViewUserTitle.setVisibility(View.GONE);
	}

	public void configureServices(List<String> services) {
		Log.d("debug", "services = " + services.toString());
		for (String service : services) {
			if (service.equals(ConfigurableServices.Facebook.name()) || service.equals(ConfigurableServices.Instagram.name())
					|| service.equals(ConfigurableServices.Picasa.name()) || service.equals(ConfigurableServices.Flickr.name())) {
				textViewUserTitle.setVisibility(View.VISIBLE);
			}
			if (service.equals(ConfigurableServices.Facebook.name())) {
				linearLayoutFacebook.setVisibility(View.VISIBLE);
				dividerFacebook.setVisibility(View.VISIBLE);
			}
			if (service.equals(ConfigurableServices.Instagram.name())) {
				linearLayoutInstagram.setVisibility(View.VISIBLE);
				dividerInstagram.setVisibility(View.VISIBLE);
			}
			if (service.equals(ConfigurableServices.Picasa.name())) {
				linearLayoutPicasa.setVisibility(View.VISIBLE);
				dividerPicasa.setVisibility(View.VISIBLE);
			}
			if (service.equals(ConfigurableServices.Flickr.name())) {
				linearLayoutFlickr.setVisibility(View.VISIBLE);
				dividerFlicker.setVisibility(View.VISIBLE);
			}
		}

	}
}