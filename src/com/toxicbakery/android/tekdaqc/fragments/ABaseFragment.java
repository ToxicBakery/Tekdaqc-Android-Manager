package com.toxicbakery.android.tekdaqc.fragments;

import com.toxicbakery.android.tekdaqc.TekActivity;

import android.app.Fragment;

public abstract class ABaseFragment extends Fragment {

	public abstract String getFragmentTag();

	protected final TekActivity getTekActivity() {
		return (TekActivity) getActivity();
	}

}
