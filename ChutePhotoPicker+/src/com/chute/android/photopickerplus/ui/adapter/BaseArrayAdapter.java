package com.chute.android.photopickerplus.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import darko.imagedownloader.ImageLoader;

public class BaseArrayAdapter<T> extends BaseAdapter {

	protected ImageLoader loader;
	protected ArrayList<T> collection = new ArrayList<T>();
	protected Context context;
	protected LayoutInflater inflater;
	protected DisplayMetrics displayMetrics;
	protected boolean dualFragments;


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ((ArrayList<T>) collection).size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return collection.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void changeData(final ArrayList<T> collection) {
		this.collection = collection;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
