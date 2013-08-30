package com.chute.android.photopickerplus.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaDataModel {

  @JsonProperty("image_url")
  private String imageUrl;
  @JsonProperty("thumbnail")
  private String thumbnail;

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

}
