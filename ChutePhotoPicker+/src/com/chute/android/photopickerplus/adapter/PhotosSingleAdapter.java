/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.chute.android.photopickerplus.R;
import com.chute.sdk.collections.GCAccountMediaCollection;
import com.chute.sdk.model.GCAccountMediaModel;
import com.darko.imagedownloader.ImageLoader;

public class PhotosSingleAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private static final String TAG = PhotosSingleAdapter.class.getSimpleName();
	private static LayoutInflater inflater;
	public ImageLoader loader;
	private GCAccountMediaCollection collection;
	private final DisplayMetrics displayMetrics;
	private final Activity context;

	public PhotosSingleAdapter(final Activity context,
			final GCAccountMediaCollection collection) {
		if (collection == null) {
			this.collection = new GCAccountMediaCollection();
		} else {
			this.collection = collection;
		}
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		loader = ImageLoader.getLoader(context);
		displayMetrics = context.getResources().getDisplayMetrics();
	}

	@Override
	public int getCount() {
		return collection.size();
	}

	@Override
	public GCAccountMediaModel getItem(final int position) {
		return collection.get(position);
	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	public static class ViewHolder {

		public ImageView image;
	}

	public void changeData(final GCAccountMediaCollection collection) {
		this.collection = collection;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.photos_select_adapter_single, null);
			holder = new ViewHolder();
			holder.image = (ImageView) vi.findViewById(R.id.imageViewThumb);
			holder.image.setLayoutParams(new RelativeLayout.LayoutParams(
					displayMetrics.widthPixels / 3,
					displayMetrics.widthPixels / 3));
			holder.image.setScaleType(ScaleType.CENTER_CROP);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}

		loader.displayImage(getItem(position).getThumbUrl(), holder.image);
		return vi;
	}

}
