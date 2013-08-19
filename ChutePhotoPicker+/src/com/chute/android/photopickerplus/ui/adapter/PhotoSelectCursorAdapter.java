package com.chute.android.photopickerplus.ui.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chute.android.photopickerplus.R;

import darko.imagedownloader.ImageLoader;

public class PhotoSelectCursorAdapter extends CursorAdapter implements MediaAdapter,
    OnScrollListener {

  public static final String TAG = PhotoSelectCursorAdapter.class.getSimpleName();

  private static LayoutInflater inflater = null;
  public ImageLoader loader;
  private final int dataIndex;
  public HashMap<Integer, String> tick;
  private boolean shouldLoadImages = true;
  private final DisplayMetrics displayMetrics;
  private final Context context;
  private final boolean dualFragments;
  private ArrayList<Integer> selectedItems = new ArrayList<Integer>();

  @SuppressWarnings("deprecation")
  public PhotoSelectCursorAdapter(Context context, Cursor c) {
    super(context, c);
    this.context = context;
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    loader = ImageLoader.getLoader(context);
    dataIndex = c.getColumnIndex(MediaStore.Images.Media.DATA);
    displayMetrics = context.getResources().getDisplayMetrics();
    tick = new HashMap<Integer, String>();
    dualFragments = context.getResources().getBoolean(R.bool.has_two_panes);

  }

  public static class ViewHolder {

    public ImageView imageViewThumb;
    public ImageView imageViewTick;
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    ViewHolder holder = (ViewHolder) view.getTag();
    String path = cursor.getString(dataIndex);
    holder.imageViewThumb.setTag(path);
    holder.imageViewTick.setTag(cursor.getPosition());
    if (shouldLoadImages) {
      loader.displayImage(Uri.fromFile(new File(path)).toString(), holder.imageViewThumb,
          null);
    } else {
      loader.displayImage(null, holder.imageViewThumb, null);
    }
    configureImageViewDimensions(holder.imageViewThumb);
    if (tick.containsKey(cursor.getPosition())) {
      holder.imageViewTick.setVisibility(View.VISIBLE);
      view.setBackgroundColor(context.getResources().getColor(R.color.sky_blue));
      selectedItems.add(cursor.getPosition());
    } else {
      holder.imageViewTick.setVisibility(View.GONE);
      view.setBackgroundColor(context.getResources().getColor(R.color.gray_light));
      selectedItems.remove(cursor.getPosition());
    }
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    ViewHolder holder;
    View vi = inflater.inflate(R.layout.adapter_assets, null);
    holder = new ViewHolder();
    holder.imageViewThumb = (ImageView) vi.findViewById(R.id.imageViewThumb);
    holder.imageViewTick = (ImageView) vi.findViewById(R.id.imageViewTick);
    vi.setTag(holder);
    return vi;
  }

  @Override
  public String getItem(int position) {
    final Cursor cursor = getCursor();
    cursor.moveToPosition(position);
    return cursor.getString(dataIndex);
  }

  @Override
  public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
      int totalItemCount) {
    // Do nothing

  }

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {
    switch (scrollState) {
    case OnScrollListener.SCROLL_STATE_FLING:
    case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
      shouldLoadImages = false;
      break;
    case OnScrollListener.SCROLL_STATE_IDLE:
      shouldLoadImages = true;
      notifyDataSetChanged();
      break;
    }
  }

  public ArrayList<String> getSelectedFilePaths() {
    final ArrayList<String> photos = new ArrayList<String>();
    final Iterator<String> iterator = tick.values().iterator();
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

  public void toggleTick(int position) {
    if (tick.containsKey(position)) {
      tick.remove(position);
    } else {
      tick.put(position, getItem(position));
    }
    notifyDataSetChanged();
  }

  private void configureImageViewDimensions(ImageView imageViewThumb) {
    int orientation = context.getResources().getConfiguration().orientation;
    if (!dualFragments) {
      imageViewThumb.setLayoutParams(new RelativeLayout.LayoutParams(
          displayMetrics.widthPixels / 3,
          displayMetrics.widthPixels / 3));
    } else {
      if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        imageViewThumb.setLayoutParams(new RelativeLayout.LayoutParams(
            displayMetrics.widthPixels,
            (int) (displayMetrics.heightPixels / 3.5)));
      } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        imageViewThumb.setLayoutParams(new RelativeLayout.LayoutParams(
            displayMetrics.widthPixels / 3,
            (int) (displayMetrics.widthPixels / 3.5)));
      }
    }
  }
}
