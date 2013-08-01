package com.chute.android.photopickerplus.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chute.android.photopickerplus.R;

public class EmptyFragment extends Fragment {

	public static EmptyFragment newInstance() {
		EmptyFragment frag = new EmptyFragment();
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_empty, container, false);
		return view;
	}

}
