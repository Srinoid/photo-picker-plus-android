/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

import com.chute.android.photopickerplus.R;
import com.chute.sdk.v2.model.AccountAlbumModel;
import com.chute.sdk.v2.model.AccountBaseModel;
import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.Row;
import com.chute.sdk.v2.model.enums.RowType;

import darko.imagedownloader.ImageLoader;

public class AssetAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private static final String TAG = AssetAdapter.class.getSimpleName();

	private static final int TYPE_MAX_COUNT = 2;

	private static LayoutInflater inflater;
	public ImageLoader loader;
	public HashMap<Integer, AccountMediaModel> tick;
	private final DisplayMetrics displayMetrics;
	private final Activity context;
	private final boolean dualFragments;
	private List<Row> rows;

	public AssetAdapter(Activity context, AccountBaseModel baseModel) {
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		loader = ImageLoader.getLoader(context);
		displayMetrics = context.getResources().getDisplayMetrics();
		tick = new HashMap<Integer, AccountMediaModel>();
		dualFragments = context.getResources().getBoolean(R.bool.has_two_panes);
		rows = new ArrayList<Row>();// member variable

		if (baseModel.getFiles() != null) {
			for (AccountMediaModel file : baseModel.getFiles()) {
			rows.add(file);
			}
		} else if (baseModel.getFolders() != null) {
			for (AccountAlbumModel folder : baseModel.getFolders()) {
			rows.add(folder);
			}
		}
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		return rows.get(position).getViewType();
	}

	public int getCount() {
		return rows.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {

		public ImageView imageViewThumb;
		public ImageView imageViewTick;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		int type = getItemViewType(position);
		if (convertView == null) {
			vi = inflater.inflate(R.layout.adapter_assets, null);
			holder = new ViewHolder();
			holder.imageViewThumb = (ImageView) vi.findViewById(R.id.imageViewThumb);
			configureImageViewDimensions(holder.imageViewThumb);
			holder.imageViewTick = (ImageView) vi.findViewById(R.id.imageViewTick);
			holder.imageViewTick.setTag(position);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}

		if (type == RowType.FOLDER.ordinal()) {
			holder.imageViewTick.setVisibility(View.GONE);
			holder.imageViewThumb.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.album_default));
		} else if (type == RowType.FILE.ordinal()) {
			holder.imageViewTick.setVisibility(View.VISIBLE);
			loader.displayImage(((AccountMediaModel) getItem(position)).getUrl(), holder.imageViewThumb, null);
		}

		if (tick.containsKey(position)) {
			holder.imageViewTick.setVisibility(View.VISIBLE);
			vi.setBackgroundColor(context.getResources().getColor(R.color.sky_blue));
		} else {
			holder.imageViewTick.setVisibility(View.GONE);
			vi.setBackgroundColor(context.getResources().getColor(R.color.gray_light));
		}
		return vi;
	}

	public ArrayList<AccountMediaModel> getPhotoCollection() {
		final ArrayList<AccountMediaModel> photos = new ArrayList<AccountMediaModel>();
		final Iterator<AccountMediaModel> iterator = tick.values().iterator();
		while (iterator.hasNext()) {
			photos.add(iterator.next());
		}
		return photos;
	}

	public ArrayList<Integer> getSelectedItemPositions() {
		final ArrayList<Integer> positions = new ArrayList<Integer>();
		final Iterator<Integer> iterator = tick.keySet().iterator();
		while (iterator.hasNext()) {
			positions.add(iterator.next());
		}
		return positions;
	}

	public boolean hasSelectedItems() {
		return tick.size() > 0;
	}

	public int getSelectedItemsCount() {
		return tick.size();
	}

	public void toggleTick(final int position) {
		if (tick.containsKey(position)) {
			tick.remove(position);
		} else {
			tick.put(position, (AccountMediaModel) getItem(position));
		}
		notifyDataSetChanged();
	}

	private void configureImageViewDimensions(ImageView imageViewThumb) {
		int orientation = context.getResources().getConfiguration().orientation;
		if (!dualFragments) {
			if (orientation == Configuration.ORIENTATION_PORTRAIT) {
				int imageHeight = displayMetrics.widthPixels - 80;
				imageViewThumb.setLayoutParams(new RelativeLayout.LayoutParams(displayMetrics.widthPixels / 3,
						imageHeight / 3));
			} else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				int imageHeight = displayMetrics.widthPixels - 120;
				imageViewThumb.setLayoutParams(new RelativeLayout.LayoutParams(displayMetrics.widthPixels / 5,
						imageHeight / 5));
			}
		} else {
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				imageViewThumb.setLayoutParams(new RelativeLayout.LayoutParams(displayMetrics.widthPixels,
						(int) (displayMetrics.heightPixels / 3.5)));
			} else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
				imageViewThumb.setLayoutParams(new RelativeLayout.LayoutParams(displayMetrics.widthPixels / 3,
						(int) (displayMetrics.widthPixels / 3.5)));
			}
		}
	}
}
