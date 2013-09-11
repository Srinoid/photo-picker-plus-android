/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.models;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.araneaapps.android.libs.logger.ALog;
import com.chute.sdk.v2.model.AssetModel;
import com.chute.sdk.v2.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

/**
 * The {@link MediaModel} class encapsulates data needed for sending POST
 * request to the server.
 * 
 * The request should contain {@link OptionsModel} as well as
 * {@link MediaDataModel}. As a result, {@link AssetModel} information is
 * returned which is necessary for delivering a result to the main activity i.e.
 * the activity that started the PhotoPicker.
 * 
 */
@JsonFilter("imageDataModelFilter")
public class MediaModel implements Parcelable {

  /**
   * {@link OptionsModel} object containing the client ID.
   */
  @JsonProperty("options")
  private OptionsModel options;

  /**
   * List of {@link MediaDataModel} containing asset's image and thumbnail URL.
   */
  @JsonProperty("media")
  private List<MediaDataModel> media = new ArrayList<MediaDataModel>();

  public MediaModel() {
    media = new ArrayList<MediaDataModel>();
  }

  public OptionsModel getOptions() {
    return options;
  }

  public void setOptions(OptionsModel options) {
    this.options = options;
  }

  public List<MediaDataModel> getMedia() {
    return media;
  }

  public void setMedia(List<MediaDataModel> media) {
    this.media = media;
  }

  public String serializeImageDataModel() {
    FilterProvider filters = new SimpleFilterProvider().addFilter("imageDataModelFilter",
        SimpleBeanPropertyFilter.filterOutAllExcept("options", "media"));
    String result = null;
    try {
      result = JsonUtil.getMapper().writer(filters).writeValueAsString(this);
    } catch (JsonProcessingException e) {
      ALog.d("", e);
    }
    return result;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("ImageDataModel [options=");
    builder.append(options);
    builder.append(", media=");
    builder.append(media);
    builder.append("]");
    return builder.toString();
  }

  /*
   * (non-Javadoc)
   * @see android.os.Parcelable#describeContents()
   */
  @Override
  public int describeContents() {
    return 0;
  }

  /*
   * (non-Javadoc)
   * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
   */
  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(options, flags);
    dest.writeTypedList(media);

  }

  public MediaModel(Parcel in) {
    this();
    options = in.readParcelable(OptionsModel.class.getClassLoader());
    in.readTypedList(media, MediaDataModel.CREATOR);

  }

  public static final Parcelable.Creator<MediaModel> CREATOR = new Parcelable.Creator<MediaModel>() {

    @Override
    public MediaModel createFromParcel(Parcel in) {
      return new MediaModel(in);
    }

    @Override
    public MediaModel[] newArray(int size) {
      return new MediaModel[size];
    }

  };

}
