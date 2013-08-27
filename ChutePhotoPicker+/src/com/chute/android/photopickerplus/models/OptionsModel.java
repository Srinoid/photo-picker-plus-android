package com.chute.android.photopickerplus.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OptionsModel {

  @JsonProperty("client_id")
  private String cliendId;

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

}
