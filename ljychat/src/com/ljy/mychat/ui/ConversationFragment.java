package com.ljy.mychat.ui;

import java.util.ArrayList;

import com.ljy.ljychat.R;
import com.ljy.mychat.utils.ExpressionUtil;
import com.ljy.mychat.utils.LiLog;
import com.ljy.mychat.utils.MustCommonUtils;
import com.ljy.mychat.utils.MyDatabaseHelper;
import com.ljy.mychat.xmpp.InitApplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ConversationFragment extends Fragment{
	
	private ListView conversation_list;
	private ArrayList<String> friends = new ArrayList<String>();
	private ArrayList<String> lastMessage = new ArrayList<String>();
	private ArrayList<String> lastTime = new ArrayList<String>();
	private String my_name;
	private MyDatabaseHelper dbhelper;
	private ConversationListReceiver csr;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbhelper = new MyDatabaseHelper(getActivity(), getActivity().getFilesDir().getAbsolutePath()
				+"/invitefriend.db3", 1);
		csr = new ConversationListReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.li.conversationlistupdate");
		getActivity().registerReceiver(csr, filter);
		LiLog.d("onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.ljy_fragment_conversation, null);
		my_name = MustCommonUtils.getInstance().getCurrentUserName();
		conversation_list = (ListView) root.findViewById(R.id.conversation_listview);
		
		conversation_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tv = (TextView) view.findViewById(R.id.ljy_chat_name_new);
				Intent intent =  new Intent(getActivity(),ChatActivity.class);
				intent.putExtra("chat_name", tv.getText());
				startActivity(intent);
			}
			
		});
		LiLog.d("onCreateView");
		return root;
	}
	
	@Override
	public void onStart() {
		LiLog.d("onStart");
		super.onStart();
		new Thread(new Runnable() {
			@Override
			public void run() {
				selectData();
				handler.sendEmptyMessage(1);
			}
		}).start();
	}
	
	public void selectData(){
		try{
			String sql = "select * from conversation where sender=?";
			LiLog.d("conversation:1");
			Cursor cursor = dbhelper.getReadableDatabase().rawQuery(sql, new String[]{my_name});
			LiLog.d("conversation:2");
			if(cursor == null || cursor.getCount() == 0 ){
				return;
			}
			LiLog.d("conversation:3");
			friends.clear();
			lastMessage.clear();
			lastTime.clear();
			while(cursor.moveToNext()){
				String from = cursor.getString(1);
				String content = cursor.getString(2);
				String time = cursor.getString(3);
				LiLog.d("conversation:5");
				friends.add(from);
				lastMessage.add(content);
				lastTime.add(time);
			}
			LiLog.d("conversation:4");
		} catch (Exception e) {
			LiLog.d("conversation exception:"+e.getMessage());
		}
	}
	private void initConversationListView() {
		if(friends.size() == 0){
			return;
		}
		adapter.notifyDataSetChanged();
		conversation_list.setAdapter(adapter);
	}
	
	class ViewHolder{
		TextView cfriend;
		TextView ccontent;
		TextView ctime;
	}
	
	BaseAdapter adapter = new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if(convertView == null){
				vh = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).
						inflate(R.layout.ljy_row_chat_history, null);
				vh.cfriend = (TextView) convertView.findViewById(R.id.ljy_chat_name_new);
				vh.ccontent = (TextView) convertView.findViewById(R.id.ljy_mychat_message_new);
				vh.ctime = (TextView) convertView.findViewById(R.id.ljy_mychat_time_new);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
			vh.cfriend.setText(friends.get(position));
			vh.ccontent.setText(ExpressionUtil.getText(getActivity(), lastMessage.get(position), "1"));
			vh.ctime.setText(lastTime.get(position));
			return convertView;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			return friends.get(position);
		}
		
		@Override
		public int getCount() {
			return friends.size();
		}
	};
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1){
				initConversationListView();
			}
		}
	};
	
	@Override
	public boolean getUserVisibleHint() {
		LiLog.d("getUserVisibleHint");
		return super.getUserVisibleHint();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		LiLog.d("onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		LiLog.d("onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onDestroy() {
		LiLog.d("onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		LiLog.d("onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		LiLog.d("onDetach");
		super.onDetach();
		getActivity().unregisterReceiver(csr);
	}

	@Override
	public void onPause() {
		LiLog.d("onPause");
		super.onPause();
	}

	@Override
	public void onResume() {
		LiLog.d("onResume");
		super.onResume();
	}

	class ConversationListReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			selectData();
			handler.sendEmptyMessage(1);
		}
		
	}
	
	
}
