package com.toxicbakery.android.tekdaqc;

import android.app.Activity;
import android.os.Bundle;

import com.tenkiv.tekdaqc.ATekDAQC;
import com.tenkiv.tekdaqc.TekCast;
import com.tenkiv.tekdaqc.peripherals.analog.AAnalogInput;
import com.toxicbakery.android.tekdaqc.fragments.ABaseFragment;
import com.toxicbakery.android.tekdaqc.fragments.DetailsFragment;
import com.toxicbakery.android.tekdaqc.fragments.DevicesListFragment;
import com.toxicbakery.android.tekdaqc.fragments.DevicesListFragment.OnDeviceSelected;

public class TekActivity extends Activity implements OnDeviceSelected {

	private static final int PANE_LEFT = R.id.container_left;
	private static final int PANE_RIGHT = R.id.container_right;

	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Determine if UI is two pane
		mTwoPane = findViewById(PANE_RIGHT) != null;

		replaceFragment(mTwoPane ? new DetailsFragment() : new DevicesListFragment());
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		if (getFragmentManager().getBackStackEntryCount() == 0)
			finish();
	}

	@Override
	public void onDeviceSelected(ATekDAQC<? extends AAnalogInput> board) {
		final Bundle bundle = new Bundle();
		bundle.putSerializable(TekCast.EXTRA_TEK_BOARD, board);

		final DetailsFragment frag = new DetailsFragment();
		frag.setArguments(bundle);

		// Only replace the fragment if it is not already visible
		if (!mTwoPane)
			replaceFragment(frag);
	}

	public final boolean isTwoPane() {
		return mTwoPane;
	}

	/**
	 * Helper method to replace the fragment location of detailed views.
	 * 
	 * @param frag
	 */
	private final void replaceFragment(ABaseFragment frag) {
		getFragmentManager().beginTransaction().addToBackStack(null)
				.replace(mTwoPane ? PANE_RIGHT : PANE_LEFT, frag, frag.getFragmentTag()).commit();
	}

}
