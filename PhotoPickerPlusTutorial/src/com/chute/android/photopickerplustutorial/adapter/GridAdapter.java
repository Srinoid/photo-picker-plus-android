/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplustutorial.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chute.android.photopickerplustutorial.R;
import com.chute.sdk.v2.model.AssetModel;

import darko.imagedownloader.ImageLoader;

public class GridAdapter extends BaseAdapter {

  public static final String TAG = GridAdapter.class.getSimpleName();
  private static LayoutInflater inflater;
  public ImageLoader loader;
  private ArrayList<AssetModel> collection;
  private final DisplayMetrics displayMetrics;
  private final int orientation;
  private Activity context;

  public GridAdapter(final Activity context, final ArrayList<AssetModel> collection) {
    if (collection == null) {
      this.collection = new ArrayList<AssetModel>();
    } else {
      this.collection = collection;
    }
    loader = ImageLoader.getLoader(context);
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    displayMetrics = context.getResources().getDisplayMetrics();
    orientation = context.getResources().getConfiguration().orientation;
    this.context = context;
  }

  @Override
  public int getCount() {
    return collection.size();
  }

  @Override
  public AssetModel getItem(int position) {
    return collection.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  public static class ViewHolder {

    public ImageView image;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View vi = convertView;
    ViewHolder holder;
    if (convertView == null) {
      vi = inflater.inflate(R.layout.grid_adapter_item, null);
      holder = new ViewHolder();
      holder.image = (ImageView) vi.findViewById(R.id.imageViewThumb);
      configureImageViewDimensions(holder.image);
      vi.setTag(holder);
    } else {
      holder = (ViewHolder) vi.getTag();
    }
    loader.displayImage(getItem(position).getThumbnail(), holder.image, null);
    return vi;
  }

  public void changeData(ArrayList<AssetModel> collection) {
    this.collection = collection;
    notifyDataSetChanged();
  }

  private void configureImageViewDimensions(ImageView imageViewThumb) {
    int gridColumns = 0;
    int imageDimension = 0;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      gridColumns = context.getResources().getInteger(R.integer.grid_columns_portrait);
      imageDimension = context.getResources().getInteger(R.integer.asset_width_portrait);
    } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
      gridColumns = context.getResources().getInteger(R.integer.grid_columns_landscape);
      imageDimension = context.getResources().getInteger(R.integer.asset_width_landscape);
    }
    imageViewThumb.setLayoutParams(new RelativeLayout.LayoutParams(
        displayMetrics.widthPixels
            / gridColumns, (displayMetrics.widthPixels - imageDimension)
            / gridColumns));
  }

}
