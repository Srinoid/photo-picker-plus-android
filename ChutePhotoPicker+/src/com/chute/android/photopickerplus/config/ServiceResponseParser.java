package com.chute.android.photopickerplus.config;

import java.io.InputStream;

import com.dg.libs.rest.parsers.BaseJacksonMapperResponseParser;

public class ServiceResponseParser extends
    BaseJacksonMapperResponseParser<ServiceResponseModel> {

  public static final String TAG = ServiceResponseParser.class.getSimpleName();

  @Override
  public ServiceResponseModel parse(InputStream responseBody) throws Exception {
    return mapper.readValue(responseBody,
        mapper.getTypeFactory().constructType(ServiceResponseModel.class));
  }

}
