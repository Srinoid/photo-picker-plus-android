/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.config;

import java.io.InputStream;

import com.dg.libs.rest.parsers.BaseJacksonMapperResponseParser;

/**
 * The {@link ServiceResponseParser} is used for parsing the JSON response from
 * the server and converting it to {@link ServiceResponseModel} objects used
 * throughout the application.
 * 
 */
public class ServiceResponseParser extends
    BaseJacksonMapperResponseParser<ServiceResponseModel> {

  public static final String TAG = ServiceResponseParser.class.getSimpleName();

  @Override
  public ServiceResponseModel parse(InputStream responseBody) throws Exception {
    return mapper.readValue(responseBody,
        mapper.getTypeFactory().constructType(ServiceResponseModel.class));
  }

}
