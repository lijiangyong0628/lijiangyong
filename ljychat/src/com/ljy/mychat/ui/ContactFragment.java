package com.ljy.mychat.ui;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ljy.ljychat.R;
import com.ljy.mychat.model.Friend;
import com.ljy.mychat.utils.LiLog;
import com.ljy.mychat.utils.PinyinUtils;
import com.ljy.mychat.widget.TitleBar;
import com.ljy.mychat.xmpp.XmppConnection;

public class ContactFragment extends Fragment implements OnItemClickListener{

	private TitleBar titleBar;
	private ListView contacts_listview;
	private List<Friend> list;
	private RelativeLayout rl_applyAndMessage;
	private int headflag = 0 ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		LiLog.d("Contactfragment:onCreated");
		headflag = 0;
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LiLog.d("ContactFragment:onCreateview start");
		View root = inflater.inflate(R.layout.ljy_fragment_contact, null);
		contacts_listview = (ListView) root
				.findViewById(R.id.contacts_listview);

		titleBar = (TitleBar) root.findViewById(R.id.contact_title_bar);
		titleBar.setRightLayoutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),
						AddContactActivity.class));
			}
		});
		rl_applyAndMessage = (RelativeLayout) LayoutInflater.from(getActivity()).
				inflate(R.layout.ljy_listviee_headerview, contacts_listview,false);
		rl_applyAndMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),
						AddFriendManagerActivity.class));
			}
		});
		
		contacts_listview.setOnItemClickListener(this);
		LiLog.d("onCreateview end");
		return root;
	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				contacts_listview.addHeaderView(rl_applyAndMessage);
				contacts_listview.setAdapter(adapter);
				headflag = 1;
			}
			if(msg.what == 2){
				contacts_listview.setAdapter(adapter);
			}
		}
	};

	class ViewHolder {
		TextView letter;
		TextView tv;
	}

	BaseAdapter adapter = new BaseAdapter() {

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder vh = null;
			if (null == convertView) {
				vh = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.convertview_contactfragment_listview, null);
				vh.tv = (TextView) convertView.findViewById(R.id.mychat_name2);
				vh.letter = (TextView) convertView
						.findViewById(R.id.mychat_header2);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			vh.tv.setText(list.get(position).username);
			if (position == 0) {
				vh.letter.setVisibility(View.VISIBLE);
				vh.letter.setText(PinyinUtils
						.getPingYin(list.get(position).username)
						.substring(0, 1).toUpperCase());
			} else {
				String abc = PinyinUtils
						.getPingYin(list.get(position).username)
						.substring(0, 1);
				String lastabc = PinyinUtils.getPingYin(
						list.get(position - 1).username).substring(0, 1);
				if (abc.equalsIgnoreCase(lastabc)) {
					vh.letter.setVisibility(View.GONE);
				} else {
					vh.letter.setVisibility(View.VISIBLE);
					vh.letter.setText(abc.toUpperCase());
				}
			}
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}	
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TextView tv = (TextView) view.findViewById(R.id.mychat_name2);
		Intent intent =  new Intent(getActivity(),ChatActivity.class);
		intent.putExtra("chat_name", tv.getText());
		startActivity(intent);
	}

	@Override
	public void onPause() {
		super.onPause();
		LiLog.d("contactFragment:onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		LiLog.d("contactFragment:onResume");
	}

	@Override
	public void onStart() {
		super.onStart();
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				list = XmppConnection.getInstance().getFriends();
				if(headflag == 0){
					hand.sendEmptyMessage(1);
				}else{
					hand.sendEmptyMessage(2);
				}
			}

		}).start();
		
		LiLog.d("contactFragment:onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		LiLog.d("contactFragment:onStop");
	}
	
	

}