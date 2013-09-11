/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.config;

import java.util.Arrays;
import java.util.List;

import android.content.Context;

import com.chute.android.photopickerplus.models.enums.LocalMediaType;
import com.chute.sdk.v2.model.enums.AccountType;

/**
 * Use this builder to construct a PhotoPicker instance when you need to set
 * configuration options other than the default. PhotoPickerConfiguration is
 * best used by creating it, and then invoking its various configuration
 * methods, and finally calling build. The following is an example shows how to
 * use the PhotoPickerConfiguration to construct a PhotoPicker instance:
 * 
 * <code>
 *  PhotoPickerConfiguration config = new PhotoPickerConfiguration.Builder(
 *  getApplicationContext())
 *  .isMultiPicker(true)
 *  .accountList(AccountType.FLICKR, AccountType.FACEBOOK)
 *  .localMediaList(LocalMediaType.ALL_PHOTOS, LocalMediaType.CAMERA_PHOTOS)
 *  .configUrl("http://s3.amazonaws.com/store.getchute.com/51eeae5e6e29310c9a000001")
 * .build();
 * PhotoPicker.getInstance().init(config);
 * </code>
 * 
 * NOTE: the order of invocation of configuration methods does not matter.
 * 
 */
public final class PhotoPickerConfiguration {

  final Context context;
  final List<AccountType> accountList;
  final List<LocalMediaType> localMediaList;
  final String configUrl;
  final boolean isMultiPicker;

  private PhotoPickerConfiguration(final Builder builder) {
    context = builder.context;
    isMultiPicker = builder.isMultiPicker;
    accountList = builder.accountList;
    localMediaList = builder.localMediaList;
    configUrl = builder.configUrl;
  }

  /**
   * Creates default configuration for {@link PhotoPicker} <br />
   * <b>Default values:</b>
   * <ul>
   * <li>isMultiPicker = false</li>
   * <li>accountList = {@link AccountType#FACEBOOK},
   * {@link AccountType#INSTAGRAM}</li>
   * <li>localMediaList = {@link LocalMediaType#ALL_PHOTOS},
   * {@link LocalMediaType#CAMERA_PHOTOS}</li>
   * </ul>
   * */
  public static PhotoPickerConfiguration createDefault(Context context) {
    return new Builder(context).build();
  }

  /**
   * Builder for {@link PhotoPickerConfiguration}
   * 
   */
  public static class Builder {

    private Context context;
    private boolean isMultiPicker = false;
    private List<AccountType> accountList = null;
    private List<LocalMediaType> localMediaList = null;
    private String configUrl = null;

    public Builder(Context context) {
      this.context = context.getApplicationContext();
    }

    /** Builds configured {@link PhotoPickerConfiguration} object */
    public PhotoPickerConfiguration build() {
      initEmptyFieldsWithDefaultValues();
      return new PhotoPickerConfiguration(this);
    }

    /**
     * Sets list of remote services.
     * 
     * @param accountList
     *          List of {@link AccountType} services.
     */
    public Builder accountList(AccountType... accountList) {
      this.accountList = Arrays.asList(accountList);
      return this;
    }

    /**
     * Sets list of local services.
     * 
     * @param localMediaList
     *          List of {@link LocalMediaType} services.
     */
    public Builder localMediaList(LocalMediaType... localMediaList) {
      this.localMediaList = Arrays.asList(localMediaList);
      return this;
    }

    /**
     * Sets the URL giving the base location of the config file containing the
     * services.
     * 
     * @param configUrl
     */
    public Builder configUrl(String configUrl) {
      this.configUrl = configUrl;
      return this;
    }

    /**
     * Allows selection of one or multiple photos in the PhotoPicker.
     * 
     * @param isMultiPicker
     *          <b>true</b> for enabling multi-picking and <b>false</b> for
     *          enabling single-picking feature.
     * @return
     */
    public Builder isMultiPicker(boolean isMultiPicker) {
      this.isMultiPicker = isMultiPicker;
      return this;
    }

    private void initEmptyFieldsWithDefaultValues() {
      if (localMediaList == null && accountList == null) {
        localMediaList = DefaultConfigurationFactory.createLocalMediaList();
        accountList = DefaultConfigurationFactory.createAccountTypeList();
      }
    }
  }
}
