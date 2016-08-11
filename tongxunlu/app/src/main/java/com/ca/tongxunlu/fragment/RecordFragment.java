package com.ca.tongxunlu.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ca.tongxunlu.R;
import com.ca.tongxunlu.ui.Dialed_calls;
import com.ca.tongxunlu.ui.Missed_calls;
import com.ca.tongxunlu.ui.Received_calls;
import com.ca.tongxunlu.utils.Utils;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-11 上午 11:54
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class RecordFragment extends Fragment implements View.OnClickListener {
    TextView dialed_calls, missed_calls, received_calls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.recordxml, null);
        init(view);
        return view;
    }

    private void init(View view) {
        dialed_calls = (TextView) view.findViewById(R.id.dialed_calls);
        received_calls = (TextView) view.findViewById(R.id.received_calls);
        missed_calls = (TextView) view.findViewById(R.id.missed_calls);
        dialed_calls.setOnClickListener(this);
        received_calls.setOnClickListener(this);
        missed_calls.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialed_calls:
                Utils.simpleIntent(getActivity(), Dialed_calls.class);
                break;
            case R.id.missed_calls:
                Utils.simpleIntent(getActivity(), Missed_calls.class);
                break;
            case R.id.received_calls:
                Utils.simpleIntent(getActivity(), Received_calls.class);
                break;
        }
    }
}
