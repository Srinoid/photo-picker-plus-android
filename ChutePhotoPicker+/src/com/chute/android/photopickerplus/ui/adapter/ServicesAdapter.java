/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.dao.MediaDAO;
import com.chute.android.photopickerplus.models.enums.LocalMediaType;
import com.chute.android.photopickerplus.ui.fragment.FragmentServices.ServiceClickedListener;
import com.chute.android.photopickerplus.util.AppUtil;
import com.chute.sdk.v2.model.enums.AccountType;

import darko.imagedownloader.ImageLoader;

public class ServicesAdapter extends BaseAdapter {

  private static final int VIEW_TYPE_REMOTE_ACCOUNT = 1;
  private static final int VIEW_TYPE_LOCAL_ACCOUNT = 0;

  @SuppressWarnings("unused")
  private static final String TAG = ServicesAdapter.class.getSimpleName();

  private static LayoutInflater inflater;
  public ImageLoader loader;
  private final DisplayMetrics displayMetrics;
  private final Activity context;

  private List<AccountType> remoteAccounts = new ArrayList<AccountType>();
  private List<LocalMediaType> localAccounts = new ArrayList<LocalMediaType>();
  private ServiceClickedListener serviceClickedListener;

  public ServicesAdapter(final Activity context, List<AccountType> remoteAccounts,
      List<LocalMediaType> localAccounts, ServiceClickedListener serviceClickedListener) {
    this.context = context;
    this.remoteAccounts = remoteAccounts;
    this.localAccounts = localAccounts;
    this.serviceClickedListener = serviceClickedListener;
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    loader = ImageLoader.getLoader(context);
    displayMetrics = context.getResources().getDisplayMetrics();
  }

  @Override
  public int getCount() {
    return remoteAccounts.size() + localAccounts.size();
  }

  @Override
  public Object getItem(final int position) {
    throw new UnsupportedOperationException();
  }

  @Override
  public long getItemId(final int position) {
    return position;
  }

  @Override
  public int getItemViewType(int position) {
    // local accounts will come first in the adapter
    if (position < localAccounts.size()) {
      // Its type Local Account
      return VIEW_TYPE_LOCAL_ACCOUNT;
    }
    return VIEW_TYPE_REMOTE_ACCOUNT;
  }

  public LocalMediaType getLocalAccount(int position) {
    return localAccounts.get(position);
  }

  public AccountType getRemoteAccount(int position) {
    return remoteAccounts.get(position - localAccounts.size());
  }

  @Override
  public int getViewTypeCount() {
    return 2;
  }

  public static class ViewHolder {

    public ImageView imageView;
    public TextView textViewServiceTitle;

  }

  @Override
  public View getView(final int position, final View convertView, final ViewGroup parent) {
    View vi = convertView;
    ViewHolder holder;
    if (convertView == null) {
      vi = inflater.inflate(R.layout.adapter_services, null);
      holder = new ViewHolder();
      holder.imageView = (ImageView) vi.findViewById(R.id.imageViewService);
      holder.textViewServiceTitle = (TextView) vi.findViewById(R.id.textViewServiceTitle);
      AppUtil.configureServiceImageViewDimensions(context, holder.imageView,
          holder.textViewServiceTitle);
      vi.setTag(holder);
    } else {
      holder = (ViewHolder) vi.getTag();
    }

    if (getItemViewType(position) == VIEW_TYPE_LOCAL_ACCOUNT) {
      // Set an image as background
      setupLocalService(holder, getLocalAccount(position));
    } else {
      setupRemoteService(holder, getRemoteAccount(position));
    }
    return vi;
  }

  @SuppressWarnings("deprecation")
  private void setupLocalService(ViewHolder holder, LocalMediaType type) {
    Uri uriAllPhotos = MediaDAO
        .getLastPhotoFromAllPhotos(context.getApplicationContext());
    Uri uriLastPhotoFromCameraPhotos = MediaDAO.getLastPhotoFromCameraPhotos(context
        .getApplicationContext());
    switch (type) {
    case TAKE_PHOTO:
      holder.imageView.setBackgroundResource(R.drawable.take_photo);
      holder.textViewServiceTitle.setText(R.string.take_photos);
      break;
    case CAMERA_PHOTOS:
      if (uriLastPhotoFromCameraPhotos != null) {
        loader.displayImage(uriLastPhotoFromCameraPhotos.toString(), holder.imageView,
            null);
      } else {
        holder.imageView.setBackgroundResource(R.drawable.photo_placeholder);
      }
      holder.textViewServiceTitle.setText(R.string.camera_shots);
      break;
    case LAST_TAKEN_PHOTO:
      if (uriLastPhotoFromCameraPhotos != null) {
        loader.displayImage(uriLastPhotoFromCameraPhotos.toString(), holder.imageView,
            null);
      } else {
        holder.imageView.setBackgroundDrawable(context.getResources().getDrawable(
            R.drawable.photo_placeholder));
      }
      holder.textViewServiceTitle.setText(context.getResources().getString(
          R.string.last_photo));
      break;
    case ALL_PHOTOS:
      if (uriAllPhotos != null) {
        loader.displayImage(uriAllPhotos.toString(), holder.imageView, null);
      } else {
        holder.imageView.setBackgroundDrawable(context.getResources().getDrawable(
            R.drawable.photo_placeholder));
      }
      holder.textViewServiceTitle.setText(context.getResources().getString(
          R.string.all_photos));
      break;
    }

    // Click listners
    switch (type) {
    case ALL_PHOTOS:
      holder.imageView.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          serviceClickedListener.photoStream();
        }
      });
      break;
    case CAMERA_PHOTOS:
      holder.imageView.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          serviceClickedListener.cameraRoll();
        }
      });

      break;
    case TAKE_PHOTO:
      holder.imageView.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          serviceClickedListener.takePhoto();
        }
      });

      break;
    case LAST_TAKEN_PHOTO:
      holder.imageView.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          serviceClickedListener.lastPhoto();
        }
      });
      break;
    }

  }

  @SuppressWarnings("deprecation")
  private void setupRemoteService(ViewHolder holder,
      final AccountType type) {
    holder.textViewServiceTitle.setVisibility(View.GONE);
    holder.imageView.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        serviceClickedListener.accountLogin(type);
      }
    });
    switch (type) {
    case FACEBOOK:
      holder.imageView.setBackgroundDrawable(context.getResources().getDrawable(
          R.drawable.facebook));
      break;
    case FLICKR:
      holder.imageView.setBackgroundDrawable(context.getResources().getDrawable(
          R.drawable.flickr));
      break;
    case INSTAGRAM:
      holder.imageView.setBackgroundDrawable(context.getResources().getDrawable(
          R.drawable.instagram));
      break;
    case PICASA:
      holder.imageView.setBackgroundDrawable(context.getResources().getDrawable(
          R.drawable.picassa));
      break;
    case GOOGLE_PLUS:
      // Replacing Picasa
      break;
    case GOOGLE_DRIVE:
      break;
    case SKYDRIVE:
      break;
    case DROPBOX:
      break;
    case CHUTE:
      break;
    case FOURSQUARE:
      break;
    case TWITTER:
      break;
    default:
      break;
    }
  }

}
