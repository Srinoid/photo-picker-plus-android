// Copyright (c) 2011, Chute Corporation. All rights reserved.
package com.chute.android.photopickerplustutorial.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceResponseModel<T> {

	public static final String TAG = ServiceResponseModel.class.getSimpleName();

	@JsonProperty("services")
	private List<T> services;

	public List<T> getServices() {
		return services;
	}

	public void setServices(List<T> services) {
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
