// Copyright (c) 2011, Chute Corporation. All rights reserved.
package com.chute.android.photopickerplus.config;

import java.util.List;

import com.chute.sdk.v2.model.enums.AccountType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceResponseModel {

  public static final String TAG = ServiceResponseModel.class.getSimpleName();

  @JsonProperty("services")
  private List<AccountType> services;

  public List<AccountType> getServices() {
    return services;
  }

  public void setServices(List<AccountType> services) {
    this.services = services;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("ServiceResponseModel [services=");
    builder.append(services);
    builder.append("]");
    return builder.toString();
  }

}
