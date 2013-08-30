package com.chute.android.photopickerplus.models;

import java.util.List;

import android.os.Parcelable;

import com.chute.sdk.v2.model.AssetModel;
import com.chute.sdk.v2.model.ParcelModel;
import com.chute.sdk.v2.model.ProfileModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({ "profile" })
public class ImageResponseModel {

  @JsonProperty("media")
  private List<AssetModel> assetModel;
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

  public List<AssetModel> getAssetModel() {
    return assetModel;
  }

  public void setAssetModel(List<AssetModel> assetModel) {
    this.assetModel = assetModel;
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
    builder.append("ImageResponseModel [assetModel=");
    builder.append(assetModel);
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

}
