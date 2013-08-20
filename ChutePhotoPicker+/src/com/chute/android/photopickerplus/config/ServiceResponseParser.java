package com.chute.android.photopickerplus.config;

import java.io.InputStream;

import com.dg.libs.rest.parsers.BaseJacksonMapperResponseParser;
import com.fasterxml.jackson.databind.DeserializationFeature;

public class ServiceResponseParser extends
    BaseJacksonMapperResponseParser<ServiceResponseModel> {

  public static final String TAG = ServiceResponseParser.class.getSimpleName();

  @Override
  public ServiceResponseModel parse(InputStream responseBody) throws Exception {
    mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    return mapper.readValue(responseBody,
        mapper.getTypeFactory().constructType(ServiceResponseModel.class));
  }

}
