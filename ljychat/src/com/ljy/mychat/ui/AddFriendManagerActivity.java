package com.ljy.mychat.ui;

import java.util.ArrayList;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ljy.ljychat.R;
import com.ljy.mychat.model.ChatItem;
import com.ljy.mychat.utils.HttpRequestCommonUtils;
import com.ljy.mychat.utils.LiLog;
import com.ljy.mychat.utils.MustCommonUtils;
import com.ljy.mychat.xmpp.InitApplication;
import com.ljy.mychat.xmpp.XmppConnection;

public class AddFriendManagerActivity extends Activity {
	
	private ListView addfriendmanger_list;
	private String result;
	private SQLiteDatabase db;
	private ArrayList<String> applylist = new ArrayList<String>();
	private String my_name;
	private InviteBroadcast broadcast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ljy_activity_addfriendmanager);
		addfriendmanger_list = (ListView) findViewById(R.id.addfriendmanger_list);
		db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getAbsolutePath()
				+"/invitefriend.db3", null);
		applylist.clear();
		my_name = MustCommonUtils.getInstance().getCurrentUserName();
		
		//注册一个广播用于接收申请请求
		broadcast = new InviteBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.ljy.invite");
		registerReceiver(broadcast, filter);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					String sql = "select * from InviteTable where acceptman=?";
					Cursor cursor = db.rawQuery(sql,new String[]{my_name});
					if(null == cursor || cursor.getCount() == 0){
						handler.sendEmptyMessage(3);
						return;
					}
					if(cursor.getCount() > 0){
						while(cursor.moveToNext()){
							applylist.add(cursor.getString(0));
						}
						handler.sendEmptyMessage(1);
					}
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(3);
					LiLog.d("select error");
				}
			}
		}).start();
	}
	
	public void initListView(){
		if(applylist.size() == 0){
			addfriendmanger_list.setVisibility(View.GONE);
			return;
		}
		addfriendmanger_list.setVisibility(View.VISIBLE);
		addfriendmanger_list.setAdapter(baseAdapter);
	}
	
	public class InviteBroadcast extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String response = intent.getStringExtra("response");
			if(response.equals("apply")){
				LiLog.d("apply");
				String fromName = intent.getStringExtra("fromName");
				String toName = intent.getStringExtra("toName");
				applylist.add(XmppConnection.getInstance()
						.getUsername(fromName));
				handler.sendEmptyMessage(1);
			}
			if(response.equals("agree")){
				LiLog.d("agree");
			}
			if(response.equals("delete")){
				LiLog.d("delete");
			}
			if(response.equals("refuse")){
				LiLog.d("refuse");
			}
		} 
		
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1){
//				addfriendmanger_list.setVisibility(View.VISIBLE);
//				addfriendmanger_list.setAdapter(baseAdapter);
				baseAdapter.notifyDataSetChanged();
				initListView();
			}
			if(msg.what == 2){
				Toast.makeText(AddFriendManagerActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
				initListView();
			}
			if(msg.what == 3){
				Toast.makeText(AddFriendManagerActivity.this, "没有好友申请", Toast.LENGTH_SHORT).show();
				initListView();
			}
			if(msg.what == 4){
				LiLog.d("收到广播动态更新listview");
			}
		}
	};
	
	protected void onStop() {
		super.onStop();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String sql2 = "delete from InviteTable where acceptman = ? ";
				String sql = "insert into InviteTable values(?,?)";
				try {
					//更新下申请的数据库
					db.execSQL(sql2,new String[]{my_name});
					for(int i = 0;i < applylist.size();i ++){
						db.execSQL(sql, new String[]{applylist.get(i),my_name});
					}
				} catch (Exception e) {
					e.printStackTrace();
					LiLog.d("已存在");
				}
			}
		}).start();
	}
	
	class ViewHolder{
		TextView tv;
	}
	
	private BaseAdapter baseAdapter = new BaseAdapter() {
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if(null == convertView){
				vh = new ViewHolder();
				convertView = LayoutInflater.from(AddFriendManagerActivity.this).inflate
						(R.layout.convertview_addfriendmanager, null);
				vh.tv = (TextView) convertView.findViewById(R.id.invite_name);
				Button but = (Button) convertView.findViewById(R.id.user_state);
				but.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						try {
							Presence pres = new Presence(Presence.Type.subscribed);
							pres.setTo(XmppConnection.getInstance().getFullUsername(applylist.get(position)));
							XmppConnection.getInstance().getConnection().sendPacket(pres);
							XmppConnection.getInstance().addUser(applylist.get(position));
							applylist.remove(position);
							handler.sendEmptyMessage(2);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
			vh.tv.setText(applylist.get(position));
			return convertView;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			return applylist.get(position);
		}
		
		@Override
		public int getCount() {
			return applylist.size();
		}
	};
	
	@Override
	protected void onResume() {
		super.onResume();	
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcast);
	}

	public void back(View v){
		finish();
	}
}
