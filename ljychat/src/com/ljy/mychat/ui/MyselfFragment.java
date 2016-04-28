package com.ljy.mychat.ui;

import com.ljy.ljychat.R;
import com.ljy.mychat.utils.MustCommonUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyselfFragment extends Fragment implements OnClickListener{
	
	private TextView my_nameTv;
	private RelativeLayout photo_rl;
	private RelativeLayout money_rl;
	private RelativeLayout map_rl;
	private RelativeLayout chat_rl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.ljy_fragment_myself, null);
		my_nameTv = (TextView) root.findViewById(R.id.ljy_my_name);
		my_nameTv.setText("慧慧号："+MustCommonUtils.getInstance().getCurrentUserName());
		photo_rl = (RelativeLayout) root.findViewById(R.id.photo_list_rl);
		money_rl = (RelativeLayout) root.findViewById(R.id.money_list_rl);
		map_rl = (RelativeLayout) root.findViewById(R.id.map_list_rl);
		chat_rl = (RelativeLayout) root.findViewById(R.id.xianchat_list_rl);
		photo_rl.setOnClickListener(this);
		money_rl.setOnClickListener(this);
		map_rl.setOnClickListener(this);
		chat_rl.setOnClickListener(this);
		return root;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.photo_list_rl:
			Toast.makeText(getActivity(), "相册功能未开启", Toast.LENGTH_SHORT).show();
			break;
		case R.id.money_list_rl:
			startActivity(new Intent(getActivity(),MoneyActivity.class));
			break;
		case R.id.map_list_rl:
			Toast.makeText(getActivity(), "地图功能未开启", Toast.LENGTH_SHORT).show();
			break;
		case R.id.xianchat_list_rl:
			Toast.makeText(getActivity(), "闲聊功能未开启", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}
	
	
	
}