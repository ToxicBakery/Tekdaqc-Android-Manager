package com.toxicbakery.android.tekdaqc.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toxicbakery.android.tekdaqc.R;

public class DetailsFragment extends ABaseFragment {

	private static final String TAG = DetailsFragment.class.getSimpleName() + ".TAG";

	@Override
	public String getFragmentTag() {
		return TAG;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_details, null, false);

		return view;
	}

}
