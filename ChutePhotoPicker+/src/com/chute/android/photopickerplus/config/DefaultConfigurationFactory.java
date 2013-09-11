/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.config;

import java.util.ArrayList;
import java.util.List;

import com.chute.android.photopickerplus.models.enums.LocalMediaType;
import com.chute.sdk.v2.model.enums.AccountType;

/**
 * Factory providing default options for {@linkplain PhotoPickerConfiguration
 * configuration}.
 * 
 */
public class DefaultConfigurationFactory {

  /**
   * Creates default implementation of local media services.
   * 
   * @return List of {@link LocalMediaType} containing All Photos and Camera
   *         Photos as default local services.
   */
  public static List<LocalMediaType> createLocalMediaList() {
    List<LocalMediaType> localMediaTypeList = new ArrayList<LocalMediaType>();
    localMediaTypeList.add(LocalMediaType.ALL_PHOTOS);
    localMediaTypeList.add(LocalMediaType.CAMERA_PHOTOS);
    return localMediaTypeList;
  }

  /**
   * Creates default implementation of remote services.
   * 
   * @return List of {@link AccountType} containing Facebook and Instagram as
   *         default remote services.
   */
  public static List<AccountType> createAccountTypeList() {
    List<AccountType> accountTypeList = new ArrayList<AccountType>();
    accountTypeList.add(AccountType.FACEBOOK);
    accountTypeList.add(AccountType.INSTAGRAM);
    return accountTypeList;
  }
}