package com.chute.android.photopickerplus.ui.fragment;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.config.ConfigServicesSingleton;
import com.chute.android.photopickerplus.models.enums.LocalMediaType;
import com.chute.android.photopickerplus.ui.adapter.ServicesAdapter;
import com.chute.sdk.v2.model.enums.AccountType;

public class FragmentServices extends Fragment {

  private GridView gridViewServices;
  private ServicesAdapter adapter;
  private ServiceClickedListener serviceClickedListener;

  public interface ServiceClickedListener {

    public void accountLogin(AccountType accountType);

    public void photoStream();

    public void cameraRoll();

    public void lastPhoto();

    public void takePhoto();

  }

  public static FragmentServices newInstance(String[] services) {
    FragmentServices frag = new FragmentServices();
    Bundle args = new Bundle();
    frag.setArguments(args);
    return frag;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    serviceClickedListener = (ServiceClickedListener) activity;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = null;
    view = inflater.inflate(R.layout.fragment_services, container, false);
    gridViewServices = (GridView) view.findViewById(R.id.gridViewServicesVertical);
    gridViewServices.setNumColumns(getResources().getInteger(
        R.integer.grid_columns_services));
    return view;
  }
  
  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ConfigServicesSingleton singleton = ConfigServicesSingleton.getInstance(getActivity());
    configureServices(singleton.getRemoteServices(), singleton.getLocalServices());
  }

  private void configureServices(List<AccountType> remoteServices, List<LocalMediaType> localServices) {
    adapter = new ServicesAdapter(getActivity(), remoteServices,localServices, serviceClickedListener);
    gridViewServices.setAdapter(adapter);
  }

}
