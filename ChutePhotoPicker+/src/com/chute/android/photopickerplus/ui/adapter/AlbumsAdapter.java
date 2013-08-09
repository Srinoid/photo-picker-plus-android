/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chute.android.photopickerplus.R;
import com.chute.sdk.v2.model.AccountAlbumModel;

public class AlbumsAdapter extends BaseAdapter {

	public static final String TAG = AlbumsAdapter.class.getSimpleName();

	private static LayoutInflater inflater;
	private final ArrayList<AccountAlbumModel> collection;
	private final Context context;

	public AlbumsAdapter(final Context context, final ArrayList<AccountAlbumModel> collection) {
		this.context = context;
		this.collection = collection;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return collection.size();
	}

	@Override
	public AccountAlbumModel getItem(final int position) {
		return collection.get(position);
	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	public class ViewHolder {
		TextView textViewAlbumName;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {

		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.adapter_albums, null);
			holder = new ViewHolder();
			holder.textViewAlbumName = (TextView) vi.findViewById(R.id.textViewAlbumName);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}
		holder.textViewAlbumName.setText(getItem(position).getName());
		return vi;

	}

}
