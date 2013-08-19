package com.chute.android.photopickerplus.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chute.android.photopickerplus.R;

import darko.imagedownloader.ImageLoader;

public class BaseArrayAdapter<T> extends BaseAdapter {

  protected ImageLoader loader;
  protected ArrayList<T> collection = new ArrayList<T>();
  protected Context context;
  protected LayoutInflater inflater;
  protected DisplayMetrics displayMetrics;
  protected boolean dualFragments;
  
  public BaseArrayAdapter(Context context) {
    super();
    this.context = context;
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    loader = ImageLoader.getLoader(context);
    displayMetrics = context.getResources().getDisplayMetrics();
    dualFragments = context.getResources().getBoolean(R.bool.has_two_panes);
  }

  @Override
  public int getCount() {
    return collection.size();
  }

  @Override
  public T getItem(int position) {
    return collection.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  public void changeData(final ArrayList<T> collection) {
    this.collection = collection;
    notifyDataSetChanged();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return null;
  }

}
