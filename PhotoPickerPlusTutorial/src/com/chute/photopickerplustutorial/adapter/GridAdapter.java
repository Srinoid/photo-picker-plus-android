package com.chute.photopickerplustutorial.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;

import com.chute.photopickerplustutorial.R;
import com.chute.sdk.collections.GCAccountMediaCollection;
import com.chute.sdk.model.GCAccountMediaModel;
import com.darko.imagedownloader.ImageLoader;

public class GridAdapter extends BaseAdapter {

	public static final String TAG = GridAdapter.class.getSimpleName();
	private static LayoutInflater inflater;
	public ImageLoader loader;
	private GCAccountMediaCollection collection;
	private final DisplayMetrics displayMetrics;

	public GridAdapter(final Activity context,
			final GCAccountMediaCollection collection) {
		if (collection == null) {
			this.collection = new GCAccountMediaCollection();
		} else {
			this.collection = collection;
		}
		loader = ImageLoader.getLoader(context);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		displayMetrics = context.getResources().getDisplayMetrics();
	}

	@Override
	public int getCount() {
		return collection.size();
	}

	@Override
	public GCAccountMediaModel getItem(int position) {
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

	public void changeData(GCAccountMediaCollection collection) {
		this.collection = collection;
		notifyDataSetChanged();
	}
}
