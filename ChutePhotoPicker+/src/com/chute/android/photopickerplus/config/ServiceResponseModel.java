// Copyright (c) 2011, Chute Corporation. All rights reserved.
package com.chute.android.photopickerplus.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceResponseModel {

  public static final String TAG = ServiceResponseModel.class.getSimpleName();

  @JsonProperty("services")
  private List<String> services;

  @JsonProperty("local_features")
  private List<String> localFeatures;

  public List<String> getServices() {
    return services;
  }

  public void setServices(List<String> services) {
    this.services = services;
  }

  public List<String> getLocalFeatures() {
    return localFeatures;
  }

  public void setLocalFeatures(List<String> localFeatures) {
    this.localFeatures = localFeatures;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("ServiceResponseModel [services=");
    builder.append(services);
    builder.append(", localFeatures=");
    builder.append(localFeatures);
    builder.append("]");
    return builder.toString();
  }

}
