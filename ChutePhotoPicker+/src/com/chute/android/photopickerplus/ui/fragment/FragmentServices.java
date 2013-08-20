package com.chute.android.photopickerplus.ui.fragment;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.chute.android.photopickerplus.R;
import com.chute.android.photopickerplus.ui.adapter.ServicesAdapter;
import com.chute.sdk.v2.model.enums.Service;

public class FragmentServices extends Fragment {

  private GridView gridViewServices;
  private ServicesAdapter adapter;

  private ServiceClickedListener serviceClickedListener;

  private Service[] services;

  public interface ServiceClickedListener {

    public void accountLogin(Service accountType);

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
    gridViewServices.setOnItemClickListener(new GridClickListener());
    gridViewServices.setNumColumns(getResources().getInteger(
        R.integer.grid_columns_services));
    return view;
  }

  public void configureServices(List<Service> servicesArray) {
    services = new Service[servicesArray.size()];
    services = servicesArray.toArray(services);
    adapter = new ServicesAdapter(getActivity(), services);
    gridViewServices.setAdapter(adapter);

  }

  private final class GridClickListener implements OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      Service service = services[position];
      switch (service) {
      case FACEBOOK:
        serviceClickedListener.accountLogin(Service.FACEBOOK);
        break;
      case FLICKR:
        serviceClickedListener.accountLogin(Service.FLICKR);
        break;
      case PICASA:
        serviceClickedListener.accountLogin(Service.PICASA);
        break;
      case INSTAGRAM:
        serviceClickedListener.accountLogin(Service.INSTAGRAM);
        break;
      case ALL_PHOTOS:
        serviceClickedListener.photoStream();
        break;
      case CAMERA_SHOTS:
        serviceClickedListener.cameraRoll();
        break;
      case TAKE_PHOTO:
        serviceClickedListener.takePhoto();
        break;
      case LAST_PHOTO_TAKEN:
        serviceClickedListener.lastPhoto();
        break;
      default:
        break;
      }
    }

  }

}
