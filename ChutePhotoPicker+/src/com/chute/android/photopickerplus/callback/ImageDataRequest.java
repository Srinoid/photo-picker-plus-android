/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.callback;

import android.content.Context;

import com.chute.android.photopickerplus.models.ImageResponseModel;
import com.chute.android.photopickerplus.models.MediaModel;
import com.chute.android.photopickerplus.util.Constants;
import com.chute.sdk.v2.api.parsers.ResponseParser;
import com.chute.sdk.v2.model.response.ResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.client.BaseRestClient.RequestMethod;
import com.dg.libs.rest.requests.StringBodyHttpRequestImpl;

/**
 * The {@link ImageDataRequest} used for exchanging messages with the server
 * (request-response). It uses the {@link StringBodyHttpRequestImpl}
 * implementation.
 * 
 */
public class ImageDataRequest extends
    StringBodyHttpRequestImpl<ResponseModel<ImageResponseModel>> {

  private MediaModel imageData;

  public ImageDataRequest(Context context, MediaModel imageData,
      HttpCallback<ResponseModel<ImageResponseModel>> callback) {
    super(context, RequestMethod.POST, new ResponseParser<ImageResponseModel>(
        ImageResponseModel.class),
        callback);
    if (imageData == null) {
      throw new IllegalArgumentException("Need to provide image data");
    }
    this.imageData = imageData;
    getClient().addHeader("Content-Type", "application/json");
  }

  @Override
  public String bodyContents() {
    return this.imageData.serializeImageDataModel();
  }

  @Override
  protected String getUrl() {
    return Constants.SELECTED_IMAGES_URL;
  }

}
