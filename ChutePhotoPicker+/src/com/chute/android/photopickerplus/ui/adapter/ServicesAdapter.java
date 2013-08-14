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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.dao.MediaDAO;
import com.chute.sdk.v2.model.enums.AccountType;

import darko.imagedownloader.ImageLoader;

public class ServicesAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private static final String TAG = ServicesAdapter.class.getSimpleName();
	private static LayoutInflater inflater;
	public ImageLoader loader;
	private final DisplayMetrics displayMetrics;
	private final Activity context;
	private final boolean dualFragments;

	private String[] services;

	public ServicesAdapter(final Activity context, final String[] services) {
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
		setImageViewBackground(holder.imageView, position);
		return vi;
	}

	private void configureImageViewDimensions(ImageView imageViewThumb) {
		int orientation = context.getResources().getConfiguration().orientation;
		if (!dualFragments) {
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				int imageDimension = displayMetrics.widthPixels - 80;
				imageViewThumb.setLayoutParams(new RelativeLayout.LayoutParams(imageDimension / 5, imageDimension / 5));
			} else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
				int imageDimension = displayMetrics.widthPixels - 70;
				imageViewThumb.setLayoutParams(new RelativeLayout.LayoutParams(imageDimension / 4, imageDimension / 4));
			}
		} else {
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				int imageDimension = displayMetrics.widthPixels - 110;
				imageViewThumb
						.setLayoutParams(new RelativeLayout.LayoutParams(imageDimension / 8, (imageDimension / 8)));
			} else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
				int imageDimension = displayMetrics.widthPixels - 70;
				imageViewThumb.setLayoutParams(new RelativeLayout.LayoutParams(imageDimension / 4, imageDimension / 4));
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void setImageViewBackground(ImageView imageView, int position) {
		Uri uriAllPhotos = MediaDAO.getLastPhotoFromAllPhotos(context.getApplicationContext());
		Uri uriLastPhotoFromCameraPhotos = MediaDAO.getLastPhotoFromCameraPhotos(context.getApplicationContext());
		String service = services[position];
		imageView.setTag(position);
		if (service.equalsIgnoreCase(AccountType.FACEBOOK.name())) {
			imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.facebook));
		}
		if (service.equalsIgnoreCase(AccountType.FLICKR.name())) {
			imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.flickr));
		}
		if (service.equalsIgnoreCase(AccountType.PICASA.name())) {
			imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.picassa));
		}
		if (service.equalsIgnoreCase(AccountType.INSTAGRAM.name())) {
			imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.instagram));
		}
		if (service.equalsIgnoreCase(AccountType.TAKE_PHOTO.name())) {
			imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.take_photo));
		}
		if (service.equalsIgnoreCase(AccountType.CAMERA_SHOTS.name())) {
			if (uriLastPhotoFromCameraPhotos != null) {
				loader.displayImage(uriLastPhotoFromCameraPhotos.toString(), imageView, null);
			} else {
				imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.photo_placeholder));
			}
		}
		if (service.equalsIgnoreCase(AccountType.LAST_PHOTO_TAKEN.name())) {
			if (uriLastPhotoFromCameraPhotos != null) {
				loader.displayImage(uriLastPhotoFromCameraPhotos.toString(), imageView, null);
			} else {
				imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.photo_placeholder));
			}
		}
		if (service.equalsIgnoreCase(AccountType.ALL_PHOTOS.name())) {
			if (uriAllPhotos != null) {
				loader.displayImage(uriAllPhotos.toString(), imageView, null);
			} else {
				imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.photo_placeholder));
			}
		}
	}

}
