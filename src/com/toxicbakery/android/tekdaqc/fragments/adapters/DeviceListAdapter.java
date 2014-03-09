package com.toxicbakery.android.tekdaqc.fragments.adapters;

import java.util.List;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tenkiv.tekdaqc.ATekDAQC;
import com.tenkiv.tekdaqc.peripherals.analog.AAnalogInput;

public class DeviceListAdapter extends BaseAdapter {

	private final LayoutInflater mInflater;
	private final List<ATekDAQC<? extends AAnalogInput>> mBoards;

	public DeviceListAdapter(LayoutInflater inflater, List<ATekDAQC<? extends AAnalogInput>> boards) {
		mInflater = inflater;
		mBoards = boards;
	}

	@Override
	public int getCount() {
		return mBoards.size();
	}

	@Override
	public ATekDAQC<? extends AAnalogInput> getItem(int pos) {
		return mBoards.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup group) {
		final ATekDAQC<? extends AAnalogInput> board = getItem(pos);
		final ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final String title = board.getTitle();

		holder.mTextViewTitle.setText(TextUtils.isEmpty(title) ? board.getSerialNumber() : title);

		return convertView;
	}

	private static final class ViewHolder {

		final TextView mTextViewTitle;

		ViewHolder(View view) {
			mTextViewTitle = (TextView) view.findViewById(android.R.id.text1);
		}

	}

}
