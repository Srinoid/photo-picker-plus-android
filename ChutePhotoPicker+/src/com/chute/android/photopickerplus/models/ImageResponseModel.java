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

public class ImageResponseModel implements Parcelable {

  @JsonProperty("media")
  @JsonDeserialize(as = ArrayList.class, contentAs = AssetModel.class)
  private List<AssetModel> assetList;
  @JsonProperty("client_id")
  private String clientId;
  @JsonProperty("created_at")
  private String createdAt;
  @JsonProperty("store_id")
  private String storeId;
  @JsonProperty("updated_at")
  private String updatedAt;
  @JsonProperty("parcel")
  private ParcelModel parcel;
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

  @Override
  public int describeContents() {
    return 0;
  }

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
