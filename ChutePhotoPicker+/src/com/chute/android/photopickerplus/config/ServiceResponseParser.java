package com.chute.android.photopickerplus.config;

import java.io.InputStream;

import com.dg.libs.rest.parsers.BaseJacksonMapperResponseParser;

public class ServiceResponseParser<T> extends
		BaseJacksonMapperResponseParser<ServiceResponseModel<T>> {

	public static final String TAG = ServiceResponseParser.class.getSimpleName();
	private final Class<?> cls;

	public ServiceResponseParser(Class<?> cls) {
		this.cls = cls;
	}

	@Override
	public ServiceResponseModel<T> parse(InputStream responseBody)
			throws Exception {
		return mapper.readValue(responseBody, mapper.getTypeFactory()
				.constructParametricType(ServiceResponseModel.class, cls));
	}
}
