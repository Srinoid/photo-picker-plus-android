/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.dao.MediaDAO;
import com.chute.android.photopickerplus.ui.activity.ServicesAlbumsActivity;
import com.chute.sdk.v2.model.enums.AccountType;

import darko.imagedownloader.ImageLoader;

public class ServicesVerticalGridAdapter extends BaseAdapter  {

	@SuppressWarnings("unused")
	private static final String TAG = ServicesVerticalGridAdapter.class.getSimpleName();
	private static LayoutInflater inflater;
	public ImageLoader loader;
	private final DisplayMetrics displayMetrics;
	private final Activity context;
	private final boolean dualFragments;
	private AccountType accountType;

	private String[] services;

	public ServicesVerticalGridAdapter(final Activity context, final String[] services) {
		this.services = services;
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		loader = ImageLoader.getLoader(context);
		displayMetrics = context.getResources().getDisplayMetrics();
		dualFragments = context.getResources().getBoolean(R.bool.has_two_panes);
	}

	@Override
	public int getCount() {
		return services.length;
	}

	@Override
	public Object getItem(final int position) {
		return position;
	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	public static class ViewHolder {
		public ImageView imageView;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.adapter_services, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) vi.findViewById(R.id.imageViewService);
			configureImageViewDimensions(holder.imageView);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}

		Uri uriAllPhotos = MediaDAO.getLastPhotoFromAllPhotos(context.getApplicationContext());
		Uri uriLastPhotoFromCameraPhotos = MediaDAO.getLastPhotoFromCameraPhotos(context.getApplicationContext());
		String service = services[position];
		holder.imageView.setTag(position);
		if (service.equals("Facebook")) {
			holder.imageView.setBackground(context.getResources().getDrawable(R.drawable.icon_facebook));
			holder.imageView.setOnClickListener(new OnLoginClickListener());
		}
		if (service.equals("Flickr")) {
			holder.imageView.setBackground(context.getResources().getDrawable(R.drawable.icon_flickr));
			holder.imageView.setOnClickListener(new OnLoginClickListener());
		}
		if (service.equals("Picasa")) {
			holder.imageView.setBackground(context.getResources().getDrawable(R.drawable.icon_picasa));
			holder.imageView.setOnClickListener(new OnLoginClickListener());
		}
		if (service.equals("Instagram")) {
			holder.imageView.setBackground(context.getResources().getDrawable(R.drawable.icon_instagram));
			holder.imageView.setOnClickListener(new OnLoginClickListener());
		}
		if (service.equals("Take Photo")) {
			holder.imageView.setBackground(context.getResources().getDrawable(R.drawable.icon_camera));
			holder.imageView.setOnClickListener(new OnTakePhotoClickListener());
		}
		if (service.equals("Camera shots")) {
			if (uriLastPhotoFromCameraPhotos != null) {
				loader.displayImage(uriLastPhotoFromCameraPhotos.toString(), holder.imageView, null);
			} else {
				holder.imageView.setBackground(context.getResources().getDrawable(R.drawable.default_thumb));
			}
			holder.imageView.setOnClickListener(new OnCameraRollClickListener());
		}
		if (service.equals("Last photo taken")) {
			if (uriLastPhotoFromCameraPhotos != null) {
				loader.displayImage(uriLastPhotoFromCameraPhotos.toString(), holder.imageView, null);
			} else {
				holder.imageView.setBackground(context.getResources().getDrawable(R.drawable.default_thumb));
			}
			holder.imageView.setOnClickListener(new OnLastPhotoTakenClickListener());
		}
		if (service.equals("All photos")) {
			if (uriAllPhotos != null) {
				loader.displayImage(uriAllPhotos.toString(), holder.imageView, null);
			} else {
				holder.imageView.setBackground(context.getResources().getDrawable(R.drawable.default_thumb));
			}
			holder.imageView.setOnClickListener(new OnPhotoStreamClickListener());
		}
		return vi;
	}

	private void configureImageViewDimensions(ImageView imageViewThumb) {
		int orientation = context.getResources().getConfiguration().orientation;
		if (!dualFragments) {
			imageViewThumb.setLayoutParams(new RelativeLayout.LayoutParams(displayMetrics.widthPixels / 3,
					displayMetrics.widthPixels / 3));
		} else {
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				int fragmentWidth = displayMetrics.widthPixels - 310;
				imageViewThumb.setLayoutParams(new RelativeLayout.LayoutParams(fragmentWidth / 4,
						(int) (displayMetrics.heightPixels / 3)));
			} else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
				int fragmentHeight = displayMetrics.heightPixels - 500;
				imageViewThumb.setLayoutParams(new RelativeLayout.LayoutParams(displayMetrics.widthPixels / 4,
						fragmentHeight / 4));
			}
		}
	}
	
	private final class OnLoginClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			String service = services[(Integer) v.getTag()];
			if (service.equalsIgnoreCase(AccountType.FACEBOOK.name())) {
				accountType = AccountType.FACEBOOK;
			}
			if (service.equalsIgnoreCase(AccountType.INSTAGRAM.name())) {
				accountType = AccountType.INSTAGRAM;
			}
			if (service.equalsIgnoreCase(AccountType.FLICKR.name())) {
				accountType = AccountType.FLICKR;
			}
			if (service.equalsIgnoreCase(AccountType.PICASA.name())) {
				accountType = AccountType.PICASA;
			}
			((ServicesAlbumsActivity)context).accountLogin(accountType);
		}

	}

	private final class OnPhotoStreamClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			((ServicesAlbumsActivity)context).photoStream();
		}
	}

	private final class OnCameraRollClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			((ServicesAlbumsActivity)context).cameraRoll();
		}

	}

	private final class OnLastPhotoTakenClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			((ServicesAlbumsActivity)context).lastPhoto();
		}

	}

	private final class OnTakePhotoClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			((ServicesAlbumsActivity)context).takePhoto();
		}

	}



}
