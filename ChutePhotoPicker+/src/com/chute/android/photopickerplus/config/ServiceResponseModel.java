/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * The following class represents the concept of the response retrieved from the
 * server and wrapped in {@link ServiceResponseModel}.
 * 
 * Each {@link ServiceResponseModel} contains a list of local and a list of
 * remote services.
 * 
 */
public class ServiceResponseModel {

  public static final String TAG = ServiceResponseModel.class.getSimpleName();

  /**
   * List of remote services.
   * 
   * The list can contain: facebook, flickr, picasa, instagram, google,
   * googledrive, skydrive or dropbox.
   */
  @JsonProperty("services")
  private List<String> services;

  /**
   * List of local services.
   * 
   * The list can contain: all_photos, take_photo, last_taken_photo and
   * camera_photos.
   */
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
