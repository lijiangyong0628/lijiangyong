package com.ljy.mychat.ui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import com.ljy.ljychat.R;
import com.ljy.mychat.model.ChatItem;
import com.ljy.mychat.model.User;
import com.ljy.mychat.utils.HttpRequestCommonUtils;
import com.ljy.mychat.utils.JSONParseUtils;
import com.ljy.mychat.utils.LiLog;
import com.ljy.mychat.utils.MustCommonUtils;
import com.ljy.mychat.xmpp.XmppConnection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddContactActivity extends Activity {
	
	private EditText searchEditText;
	private LinearLayout ll_user;
//	private TextView friend_name;
	private List<String> list = new ArrayList<String>();
	private ListView listView;
	private XMPPConnection conn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ljy_activity_addcontact);
		searchEditText = (EditText) findViewById(R.id.addfriends_edit_note);
		ll_user = (LinearLayout) findViewById(R.id.ll_user);
//		friend_name = (TextView) findViewById(R.id.friend_name);
		listView = (ListView) findViewById(R.id.addcontact_listview);
	}
	
	//返回键
	public void back(View v){
		finish();
	}
	
//	//添加好友键
//	public void addContact(View v){
////		final String friendName = friend_name.getText().toString(); 
//		Toast.makeText(this, friendName, Toast.LENGTH_SHORT).show();
//		
//		//send invite message
//		try {
//			Socket socket = new Socket
//					("http://10.20.73.17", 12530);
//			DataInputStream dis = new DataInputStream(socket.getInputStream());
//			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
	
	//查找好友键
	public void searchContact(View v){
	//	Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
		final String searchName = searchEditText.getText().toString();
		final String currentUsername = MustCommonUtils.getInstance().getCurrentUserName();
		
		if(TextUtils.isEmpty(searchName)){
			Toast.makeText(this, "输入查找的用户名", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(!TextUtils.isEmpty(currentUsername)){
			if(currentUsername.equals(searchName)){
				Toast.makeText(this, "不能查找自己", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
	new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					//先打开连接
					if(null == conn){
						conn = XmppConnection.getInstance().getConnection();
					}
					List<String> list2 = XmppConnection.getInstance().searchUser(searchName);
					list.clear();
					Roster roster =conn.getRoster();
					Collection<RosterEntry> collection = roster.getEntries();
					if(collection.size() > 0){
						to:for(int i = 0;i <list2.size();i++){
							for (RosterEntry entry : collection) {
								if(entry.getUser().contains(list2.get(i)) || currentUsername.equals(list2.get(i))){
									continue to;
								}
							}
							list.add(list2.get(i));
						}
					}else{
						for(String str : list2){
							if(! str.equals(currentUsername)){
								list.add(str);
							}
						}
					}
					if(list.size() == 0){
						hand.sendEmptyMessage(3);
					}else{
						hand.sendEmptyMessage(2);
					}
//					String uri = "http://10.20.73.17:8080/LijiangyongChatServer/useroperate";
//					String result = HttpRequestCommonUtils.sendPostResquest(uri, searchName, "addFriendsPage");
//					LiLog.d(result);
//					
//					String state = JSONParseUtils.getStateOrCode(result, "state");
//					
//					if("".equals(state)){
//						hand.sendEmptyMessage(1);
//						return;
//					}
//					if("success".equals(state)){
//						hand.sendEmptyMessage(2);
//					}else{
//						hand.sendEmptyMessage(3);
//					}
//					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	Handler hand = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//添加好友按钮事件
			if(msg.what == 1){
//				ll_user.setVisibility(View.GONE);
				Toast.makeText(AddContactActivity.this, "添加好友请求已经发送", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent("com.li.updatefragment");
				intent.putExtra("isUpdate", true);
				sendBroadcast(intent);
			}
			//搜索到好友事件
			if(msg.what == 2){
				ll_user.setVisibility(View.VISIBLE);
				listView.setAdapter(adapter);
//				friend_name.setText(searchEditText.getText().toString());
			} 
			//未搜索到好友
			if(msg.what == 3){
				ll_user.setVisibility(View.GONE);
				Toast.makeText(AddContactActivity.this, "该好友不存在", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	class ViewHolder{
		TextView tv;
	}
	
	class onAddClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			hand.sendEmptyMessage(1);
		}
		
	}
	
	BaseAdapter adapter = new BaseAdapter() {
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if(convertView == null){
				vh = new ViewHolder();
				convertView = LayoutInflater.from(AddContactActivity.this).inflate
						(R.layout.convertview_addcontact_list, null);
				vh.tv = (TextView) convertView.findViewById(R.id.friend_name);
				Button add = (Button) convertView.findViewById(R.id.friend_indicator);
				add.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								try {
									if(null == conn){
										conn = XmppConnection.getInstance().getConnection();
									}
//									Chat c = conn.getChatManager().createChat(list.get(position)+"@10.20.73.17",
//											new MessageListener() {
//												
//												@Override
//												public void processMessage(Chat arg0, Message arg1) {
//													System.out
//															.println(arg1.getBody());
////													Intent addIntent = new Intent("com.li.addFriend");
////													addIntent.set
////													sendBroadcast(addIntent);
//												}
//											});
//									c.sendMessage("add");
									XmppConnection.getInstance().addUser(list.get(position));
//									Presence p = new Presence(Presence.Type.subscribe);
//									p.setTo(XmppConnection.getInstance().getFullUsername(list.get(position)));
//									XmppConnection.getInstance().getConnection().sendPacket(p);
									hand.sendEmptyMessage(1);
//									XmppConnection.getInstance().sendMsg(list.get(position), 
//											"add", ChatItem.CHAT);
//									String uri = "http://10.20.73.17:8080/LJYchat/invite";
//									long t = System.currentTimeMillis();
//									SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd HH:mm");
//									Date d = new Date(t);
//									String time = sdf.format(d);
//									String result = HttpRequestCommonUtils.sendInviteRequest(uri,"inviteFlag",
//											MustCommonUtils.getInstance().getCurrentUserName(), 
//											list.get(position), time);
//									LiLog.d(result);
//									LiLog.d(time);
//									if(result.equals("success")){
//										hand.sendEmptyMessage(1);
//									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}).start();
					}
				});
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
			vh.tv.setText(list.get(position));
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
}
