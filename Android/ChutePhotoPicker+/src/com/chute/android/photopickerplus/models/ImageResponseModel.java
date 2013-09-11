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

import com.chute.sdk.v2.model.AssetModel;
import com.chute.sdk.v2.model.ParcelModel;
import com.chute.sdk.v2.model.ProfileModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * This class encapsulates the response that is received from the server when
 * uploading the selected photo(s) URL(s).
 * 
 * It consists of list of {@link AssetModel}s, cliend ID, time of creation and
 * update, {@link ParcelModel} and user {@link ProfileModel}. actuall content
 * sent.
 * 
 */
public class ImageResponseModel implements Parcelable {

  /**
   * List of {@link AssetModel}s
   */
  @JsonProperty("media")
  @JsonDeserialize(as = ArrayList.class, contentAs = AssetModel.class)
  private List<AssetModel> assetList;

  /**
   * Client ID
   */
  @JsonProperty("client_id")
  private String clientId;

  /**
   * Time when the {@link AssetModel} is created
   */
  @JsonProperty("created_at")
  private String createdAt;

  /**
   * Store ID
   */
  @JsonProperty("store_id")
  private String storeId;

  /**
   * Time when the {@link AssetModel} is updated
   */
  @JsonProperty("updated_at")
  private String updatedAt;

  /**
   * Parcel holding the {@link AssetModel}
   */
  @JsonProperty("parcel")
  private ParcelModel parcel;

  /**
   * User profile
   */
  @JsonProperty("profile")
  private ProfileModel profile;

  public ImageResponseModel() {
  }

  public List<AssetModel> getAssetList() {
    return assetList;
  }

  public void setAssetModel(List<AssetModel> assetList) {
    this.assetList = assetList;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public ParcelModel getParcel() {
    return parcel;
  }

  public void setParcel(ParcelModel parcel) {
    this.parcel = parcel;
  }

  public ProfileModel getProfile() {
    return profile;
  }

  public void setProfile(ProfileModel profile) {
    this.profile = profile;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("ImageResponseModel [assetList=");
    builder.append(assetList);
    builder.append(", clientId=");
    builder.append(clientId);
    builder.append(", createdAt=");
    builder.append(createdAt);
    builder.append(", storeId=");
    builder.append(storeId);
    builder.append(", updatedAt=");
    builder.append(updatedAt);
    builder.append(", parcel=");
    builder.append(parcel);
    builder.append(", profile=");
    builder.append(profile);
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
    dest.writeTypedList(assetList);
    dest.writeString(clientId);
    dest.writeString(createdAt);
    dest.writeString(storeId);
    dest.writeString(updatedAt);
    dest.writeParcelable(parcel, flags);
    dest.writeParcelable(profile, flags);

  }

  public ImageResponseModel(Parcel in) {
    this();
    in.readTypedList(assetList, AssetModel.CREATOR);
    clientId = in.readString();
    createdAt = in.readString();
    storeId = in.readString();
    updatedAt = in.readString();
    parcel = in.readParcelable(ParcelModel.class.getClassLoader());
    profile = in.readParcelable(ProfileModel.class.getClassLoader());
  }

  public static final Parcelable.Creator<ImageResponseModel> CREATOR = new Parcelable.Creator<ImageResponseModel>() {

    @Override
    public ImageResponseModel createFromParcel(Parcel in) {
      return new ImageResponseModel(in);
    }

    @Override
    public ImageResponseModel[] newArray(int size) {
      return new ImageResponseModel[size];
    }

  };
}
