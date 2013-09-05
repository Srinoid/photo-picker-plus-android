package com.chute.android.photopickerplus.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaDataModel implements Parcelable {

  @JsonProperty("image_url")
  private String imageUrl;
  @JsonProperty("thumbnail")
  private String thumbnail;

  public MediaDataModel() {
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("MediaModel [imageUrl=");
    builder.append(imageUrl);
    builder.append(", thumbnail=");
    builder.append(thumbnail);
    builder.append("]");
    return builder.toString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(imageUrl);
    dest.writeString(thumbnail);

  }

  public MediaDataModel(Parcel in) {
    this();
    imageUrl = in.readString();
    thumbnail = in.readString();
  }

  public static final Parcelable.Creator<MediaDataModel> CREATOR = new Parcelable.Creator<MediaDataModel>() {

    @Override
    public MediaDataModel createFromParcel(Parcel in) {
      return new MediaDataModel(in);
    }

    @Override
    public MediaDataModel[] newArray(int size) {
      return new MediaDataModel[size];
    }

  };

}
