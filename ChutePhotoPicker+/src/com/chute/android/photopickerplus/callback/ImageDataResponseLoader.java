package com.chute.android.photopickerplus.callback;

import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;

import com.araneaapps.android.libs.logger.ALog;
import com.chute.android.photopickerplus.models.ImageDataModel;
import com.chute.android.photopickerplus.models.OptionsModel;
import com.chute.android.photopickerplus.ui.fragment.AccountFilesListener;
import com.chute.android.photopickerplus.util.PhotoPickerPreferenceUtil;
import com.chute.sdk.v2.model.AccountMediaModel;
import com.chute.sdk.v2.model.response.ListResponseModel;
import com.dg.libs.rest.callbacks.HttpCallback;
import com.dg.libs.rest.domain.ResponseStatus;

public class ImageDataResponseLoader {

  public static void postImageData(Context context,
      ArrayList<AccountMediaModel> selectedImages, AccountFilesListener accountListener) {

    OptionsModel options = new OptionsModel();
    options.setCliendId(PhotoPickerPreferenceUtil.get().getClientId());
    ImageDataModel imageDataModel = new ImageDataModel();
    imageDataModel.setOptions(options);
    imageDataModel.setMedia(selectedImages);

    GCImage.getImageData(context, imageDataModel,
        new ImageDataCallback(context, accountListener)).executeAsync();

  }

  private static final class ImageDataCallback implements
      HttpCallback<ListResponseModel<AccountMediaModel>> {

    private Context context;
    private AccountFilesListener listener;

    private ImageDataCallback(Context context, AccountFilesListener listener) {
      this.context = context;
      this.listener = listener;
    }

    @Override
    public void onHttpError(ResponseStatus responseStatus) {
      Toast.makeText(context, "An error occurred, please try again later",
          Toast.LENGTH_SHORT).show();
      ALog.d("Http Error: " + responseStatus.getStatusCode() + " "
          + responseStatus.getStatusMessage());

    }

    @Override
    public void onSuccess(ListResponseModel<AccountMediaModel> responseData) {
      if (responseData.getData() != null) {
        listener.onDeliverAccountFiles((ArrayList<AccountMediaModel>) responseData
            .getData());
      }

    }

  }

}
