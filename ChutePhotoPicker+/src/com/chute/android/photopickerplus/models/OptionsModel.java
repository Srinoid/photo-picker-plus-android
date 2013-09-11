/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The {@link OptionsModel} class wraps up information regarding the client ID.
 * 
 */
public class OptionsModel implements Parcelable {

  /**
   * Client ID. It can be retrieved from your Chute application.
   * 
   * See <a href="https://apps.getchute.com">https://apps.getchute.com</a>
   */
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

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("OptionsModel [cliendId=");
    builder.append(cliendId);
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
