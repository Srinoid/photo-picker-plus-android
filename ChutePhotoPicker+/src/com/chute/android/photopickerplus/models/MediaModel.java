package com.chute.android.photopickerplus.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaModel {

  @JsonProperty("name")
  private String name;

  @JsonProperty("image_url")
  private String imageUrl;

  @JsonProperty("video_url")
  private String videoUrl;

  @JsonProperty("thumbnail")
  private String thumbnail;

  @JsonProperty("dimensions")
  private String dimensions;

  @JsonProperty("file_type")
  private String fileType;

  @JsonProperty("account_shortcut")
  private String accountShortcut;

  @JsonProperty("account_username")
  private String accountUsername;

  @JsonProperty("account_type")
  private String accountType;

  @JsonProperty("caption")
  private String caption;

  @JsonProperty("tags")
  private String tags;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getVideoUrl() {
    return videoUrl;
  }

  public void setVideoUrl(String videoUrl) {
    this.videoUrl = videoUrl;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public String getDimensions() {
    return dimensions;
  }

  public void setDimensions(String dimensions) {
    this.dimensions = dimensions;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getAccountShortcut() {
    return accountShortcut;
  }

  public void setAccountShortcut(String accountShortcut) {
    this.accountShortcut = accountShortcut;
  }

  public String getAccountUsername() {
    return accountUsername;
  }

  public void setAccountUsername(String accountUsername) {
    this.accountUsername = accountUsername;
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("MediaModel [name=");
    builder.append(name);
    builder.append(", imageUrl=");
    builder.append(imageUrl);
    builder.append(", videoUrl=");
    builder.append(videoUrl);
    builder.append(", thumbnail=");
    builder.append(thumbnail);
    builder.append(", dimensions=");
    builder.append(dimensions);
    builder.append(", fileType=");
    builder.append(fileType);
    builder.append(", accountShortcut=");
    builder.append(accountShortcut);
    builder.append(", accountUsername=");
    builder.append(accountUsername);
    builder.append(", accountType=");
    builder.append(accountType);
    builder.append(", caption=");
    builder.append(caption);
    builder.append(", tags=");
    builder.append(tags);
    builder.append("]");
    return builder.toString();
  }

}
