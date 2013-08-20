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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.util.AppUtil;
import com.chute.sdk.v2.model.AccountAlbumModel;
import com.chute.sdk.v2.model.AccountBaseModel;
import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.MediaViewType;
import com.chute.sdk.v2.model.enums.MediaType;

import darko.imagedownloader.ImageLoader;

public class AssetAdapter extends BaseAdapter {

  @SuppressWarnings("unused")
  private static final String TAG = AssetAdapter.class.getSimpleName();

  private static final int TYPE_MAX_COUNT = 2;

  private static LayoutInflater inflater;
  public ImageLoader loader;
  public HashMap<Integer, AccountMediaModel> tick;
  private final Activity context;
  private List<MediaViewType> rows;
  private AdapterItemClickListener listener;

  public interface AdapterItemClickListener {

    public void onFolderClicked(int position);

    public void onFileClicked(int position);
  }

  public AssetAdapter(Activity context, AccountBaseModel baseModel,
      AdapterItemClickListener listener) {
    this.context = context;
    this.listener = listener;
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    loader = ImageLoader.getLoader(context);
    tick = new HashMap<Integer, AccountMediaModel>();
    rows = new ArrayList<MediaViewType>();

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
    return rows.get(position);
  }

  public long getItemId(int position) {
    return position;
  }

  public static class ViewHolder {

    public ImageView imageViewThumb;
    public ImageView imageViewTick;
  }

  @SuppressWarnings("deprecation")
  @Override
  public View getView(final int position, final View convertView, final ViewGroup parent) {
    View vi = convertView;
    ViewHolder holder;
    int type = getItemViewType(position);
    if (convertView == null) {
      vi = inflater.inflate(R.layout.adapter_assets, null);
      holder = new ViewHolder();
      holder.imageViewThumb = (ImageView) vi.findViewById(R.id.imageViewThumb);
      AppUtil.configureImageViewDimensions(holder.imageViewThumb, context);
      holder.imageViewTick = (ImageView) vi.findViewById(R.id.imageViewTick);
      holder.imageViewTick.setTag(position);
      vi.setTag(holder);
    } else {
      holder = (ViewHolder) vi.getTag();
    }

    holder.imageViewThumb.setTag(position);
    if (type == MediaType.FOLDER.ordinal()) {
      holder.imageViewTick.setVisibility(View.GONE);
      holder.imageViewThumb.setBackgroundDrawable(context.getResources().getDrawable(
          R.drawable.album_default));
      holder.imageViewThumb.setOnClickListener(new OnFolderClickedListener());
    } else if (type == MediaType.FILE.ordinal()) {
      holder.imageViewTick.setVisibility(View.VISIBLE);
      loader.displayImage(((AccountMediaModel) getItem(position)).getThumbnail(),
          holder.imageViewThumb, null);
      holder.imageViewThumb.setOnClickListener(new OnFileClickedListener());
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

  private final class OnFolderClickedListener implements OnClickListener {

    @Override
    public void onClick(View v) {
      Integer position = (Integer) v.getTag();
      listener.onFolderClicked(position);

    }

  }

  private final class OnFileClickedListener implements OnClickListener {

    @Override
    public void onClick(View v) {
      Integer position = (Integer) v.getTag();
      listener.onFileClicked(position);

    }

  }
}
