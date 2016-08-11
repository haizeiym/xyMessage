package com.ca.tongxunlu.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ca.tongxunlu.R;
import com.ca.tongxunlu.adapter.ContactAdapter;
import com.ca.tongxunlu.model.ContactModel;
import com.ca.tongxunlu.ui.AddOrDelContactUI;
import com.ca.tongxunlu.ui.CallMenuActivity;
import com.ca.tongxunlu.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-11 上午 11:55
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: TODO
 */
public class ContactFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static ContactFragment instance;
    TextView find, back;
    ListView autoListView;
    ContactAdapter adapter;
    LinearLayout find_ll, find_in;
    List<ContactModel> contactModels = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.contactfragmentxml, null);
        find = (TextView) view.findViewById(R.id.find);
        back = (TextView) view.findViewById(R.id.back);
        autoListView = (ListView) view.findViewById(R.id.showContact);
        find_ll = (LinearLayout) view.findViewById(R.id.find_ll);
        find_in = (LinearLayout) view.findViewById(R.id.find_in);
        find_ll.setVisibility(View.VISIBLE);
        find_in.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        find.setOnClickListener(this);
        showListView(autoListView);
        return view;
    }

    private void showListView(ListView autoListView) {
        contactModels = Utils.getPeopleInPhone(getActivity());
        adapter = new ContactAdapter(getActivity(), contactModels);
        autoListView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.refresh(Utils.getPeopleInPhone(getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find:
                Utils.simpleIntent(getActivity(), AddOrDelContactUI.class);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), CallMenuActivity.class);
        intent.putExtra("num", contactModels.get(position).num);
        intent.putExtra("contact", true);
        startActivity(intent);
    }
}
