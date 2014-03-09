package com.toxicbakery.android.tekdaqc.fragments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tenkiv.tekdaqc.ATekDAQC;
import com.tenkiv.tekdaqc.BoardDiscoveredReceiver;
import com.tenkiv.tekdaqc.DiscoveryService;
import com.tenkiv.tekdaqc.DiscoveryService.ServiceAction;
import com.tenkiv.tekdaqc.LocatorParams;
import com.tenkiv.tekdaqc.TekCast;
import com.tenkiv.tekdaqc.peripherals.analog.AAnalogInput;
import com.toxicbakery.android.tekdaqc.R;
import com.toxicbakery.android.tekdaqc.fragments.adapters.DeviceListAdapter;
import com.toxicbakery.android.tekdaqc.utils.TekLog;

public class DevicesListFragment extends ABaseFragment implements OnItemClickListener {

	private static final String TAG = DevicesListFragment.class.getSimpleName() + ".TAG";

	private static final String KEY_DISCOVERED_BOARDS = "KEY_DISCOVERED_BOARDS";

	private final List<ATekDAQC<? extends AAnalogInput>> mBoards;

	private DeviceListAdapter mAdapter;

	public DevicesListFragment() {
		mBoards = Collections
				.synchronizedList(new ArrayList<ATekDAQC<? extends AAnalogInput>>());
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (!(activity instanceof OnDeviceSelected))
			throw new IllegalStateException("Activity must implement " + OnDeviceSelected.class.getSimpleName());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);

		mAdapter = new DeviceListAdapter(getActivity().getLayoutInflater(), mBoards);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			refreshDeviceList();
		} else {
			mBoards.addAll((Collection<? extends ATekDAQC<? extends AAnalogInput>>) savedInstanceState
					.getSerializable(KEY_DISCOVERED_BOARDS));
		}

		final ListView listView = new ListView(getActivity());
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);

		return listView;
	}

	@Override
	public void onResume() {
		super.onResume();

		getActivity().registerReceiver(mSearchReceiver, new IntentFilter(TekCast.ACTION_FOUND_BOARD));
	}

	@Override
	public void onPause() {
		super.onPause();

		getActivity().unregisterReceiver(mSearchReceiver);
	}

	@Override
	public String getFragmentTag() {
		return TAG;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.menu_device_list, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_device_list_refresh:
			refreshDeviceList();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putSerializable(KEY_DISCOVERED_BOARDS, (Serializable) mBoards);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
		getTekActivity().onDeviceSelected(mBoards.get(pos));
	}

	private final void refreshDeviceList() {
		mBoards.clear();
		mAdapter.notifyDataSetChanged();

		final LocatorParams.Builder builder = new LocatorParams.Builder();
		builder.setType(68);
		builder.setFirmware("0.6.0.0");

		final Intent intent = new Intent(getActivity(), DiscoveryService.class);
		intent.setAction(ServiceAction.SEARCH.toString());
		intent.putExtra(TekCast.EXTRA_LOCATOR_PARAMS, builder.build());
		getActivity().startService(intent);
	}

	private final BoardDiscoveredReceiver mSearchReceiver = new BoardDiscoveredReceiver() {

		@Override
		protected void onSearchResult(ATekDAQC<? extends AAnalogInput> board) {
			TekLog.d("Found Board: " + board.getHostIP());
			mBoards.add(board);
			mAdapter.notifyDataSetChanged();
		}

	};

	public static interface OnDeviceSelected {

		/**
		 * Called when a board is selected from the {@link DevicesListFragment}.
		 * 
		 * @param board
		 */
		public void onDeviceSelected(ATekDAQC<? extends AAnalogInput> board);

	}

}
