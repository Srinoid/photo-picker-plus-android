package com.chute.android.photopickerplus.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OptionsModel implements Parcelable {

  @JsonProperty("client_id")
  private String cliendId;

  public OptionsModel() {
  }

  public String getCliendId() {
    return cliendId;
  }

  public void setCliendId(String cliendId) {
    this.cliendId = cliendId;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("OptionsModel [cliendId=");
    builder.append(cliendId);
    builder.append("]");
    return builder.toString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(cliendId);

  }

  public OptionsModel(Parcel in) {
    this();
    cliendId = in.readString();
  }

  public static final Parcelable.Creator<OptionsModel> CREATOR = new Parcelable.Creator<OptionsModel>() {

    @Override
    public OptionsModel createFromParcel(Parcel in) {
      return new OptionsModel(in);
    }

    @Override
    public OptionsModel[] newArray(int size) {
      return new OptionsModel[size];
    }

  };

}
