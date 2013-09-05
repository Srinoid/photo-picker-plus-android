package com.chute.android.photopickerplus.models.enums;

public enum LocalMediaType {
  CAMERA_PHOTOS("camera photos"), ALL_PHOTOS("all photos"), LAST_TAKEN_PHOTO(
      "last photo taken"), TAKE_PHOTO(
      "take photo");

  private final String name;

  private LocalMediaType(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  };

  public String getName() {
    return name;
  }
}