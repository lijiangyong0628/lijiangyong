package com.ljy.mychat.ui;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.ljy.ljychat.R;
import com.ljy.mychat.utils.LiLog;
import com.ljy.mychat.utils.MustCommonUtils;
import com.ljy.mychat.xmpp.XmppConnection;
import com.ljy.mychat.xmpp.XmppMessageListener;

public class MainActivity extends FragmentActivity {
	
	private Button[] mTabs;
	private Fragment[] fragments;
	private ConversationFragment conversationFragment;
	private ContactFragment contactFragment;
	private SettingFragment settingFragment;
	private MyselfFragment myselfFragment;
	private int currentTabIndex;
	private int index;
	public static SQLiteDatabase db;
	private ChatRececiver chatreceiver;
	private XMPPConnection conn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ljy_activity_main);
		db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getAbsolutePath()
				+"/invitefriend.db3", null);
		initButton();
		initFragment();
		//添加第一个fragment
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, conversationFragment)
		.add(R.id.fragment_container, contactFragment).hide(contactFragment).show(conversationFragment)
		.commit();
			
		//注册一个广播用于更改聊天内容
		chatreceiver = new ChatRececiver();
		IntentFilter filter3 = new IntentFilter();
		filter3.addAction("com.li.chatrecevier");
		registerReceiver(chatreceiver, filter3);
		
		if(null == conn){
			conn = XmppConnection.getInstance().getConnection();
		}
		conn.getRoster().setSubscriptionMode(SubscriptionMode.manual);
		if(conn != null && conn.isConnected() && conn.isAuthenticated()){
			LiLog.d("connect correct");
			PacketFilter packFilter = new AndFilter(new PacketTypeFilter(Presence.class));
			PacketListener packetListener = new PacketListener() {
				@Override
				public void processPacket(Packet packet) {
					LiLog.d("packet:"+packet.toXML());
					if(packet instanceof Presence){
						Presence presence = (Presence) packet;
						final String from = presence.getFrom();//发送方    
	                    final String to = presence.getTo();//接收方  
	                    if (presence.getType().equals(Presence.Type.subscribe)) {    
	                        LiLog.d("收到添加请求！");  
	                        //发送广播传递发送方的JIDfrom及字符串，添加好友的申请
	                        String response = "apply";  
	                        Intent intent = new Intent();  
	                        intent.putExtra("fromName", from);  
	                        intent.putExtra("toName", to);
	                        intent.putExtra("response", response);  
	                        intent.setAction("com.ljy.invite");  
	                        sendBroadcast(intent);   
	                		new Thread(new Runnable() {
	                			@Override
	                			public void run() {
	                				String sql = "insert into InviteTable values(?,?)";
	                				try {
	                					//启动一个线程存储数据
	                					db.execSQL(sql,new String[]{XmppConnection.getInstance().getUsername(from)
	                							,XmppConnection.getInstance().getUsername(to)});
	                				} catch (Exception e) {
	                					e.printStackTrace();
	                					LiLog.d("存储出现异常");
	                				}
	                			}
	                		}).start();
	                		
	                    } else if (presence.getType().equals(Presence.Type.subscribed)) {  
	                        //发送广播传递response字符串,同意添加好友 
//							Presence pres = new Presence(Presence.Type.subscribed);
//							pres.setTo(XmppConnection.getInstance().getFullUsername(from));
//							XmppConnection.getInstance().getConnection().sendPacket(pres);
	                        String response = "agree";  
	                        Intent intent = new Intent();  
	                        intent.putExtra("response", response);  
	                        intent.setAction("com.ljy.invite");  
	                        sendBroadcast(intent);   
	                    } else if (presence.getType().equals(Presence.Type.unsubscribe)) {  
	                        //发送广播传递response字符串,删除好友的申请
	                        String response = "delete";  
	                        Intent intent = new Intent();  
	                        intent.putExtra("response", response);  
	                        intent.setAction("com.ljy.invite");  
	                        sendBroadcast(intent);   
	                    } else if (presence.getType().equals(Presence.Type.unsubscribed)){  
	                    	//拒绝添加好友的申请
	                    	 String response = "refuse";  
		                     Intent intent = new Intent();  
		                     intent.putExtra("response", response);  
		                     intent.setAction("com.ljy.invite");  
		                     sendBroadcast(intent); 
	                    } else if (presence.getType().equals(Presence.Type.unavailable)){  
	                    	//离线状态
	                         LiLog.d("好友下线！");  
	                    } else if(presence.getType().equals(Presence.Type.available)){  
	                    	//在线状态available
	                         LiLog.d("好友上线！");  
	                    } else{
	                    	LiLog.d("error");
	                    } 
					}
				}
			};
			
			conn.addPacketListener(packetListener, packFilter);
			
			XmppMessageListener messagelistener = new XmppMessageListener(this);
			conn.addPacketListener(messagelistener,new PacketTypeFilter(Message.class));
			
		}else{
			LiLog.d("connect exception");
		}
		
		
	}
	
	
	
	private void initFragment() {
		conversationFragment = new ConversationFragment();
		contactFragment = new ContactFragment();
		settingFragment = new SettingFragment();
		myselfFragment = new MyselfFragment();
		fragments = new Fragment[4];
		fragments[0] = conversationFragment;
		fragments[1] = contactFragment;
		fragments[2] = settingFragment;
		fragments[3] = myselfFragment;
	}

	private void initButton() {
		mTabs = new Button[4];
		mTabs[0] = (Button) findViewById(R.id.btn_conversation);
		mTabs[1] = (Button) findViewById(R.id.btn_address_list);
		mTabs[2] = (Button) findViewById(R.id.btn_setting);
		mTabs[3] = (Button) findViewById(R.id.btn_myself);
		mTabs[0].setSelected(true);
	}
	
	public void onTabClicked(View v){
		switch (v.getId()) {
		case R.id.btn_conversation:
			index = 0;
			break;
		case R.id.btn_address_list:
			index = 1;
			break;
		case R.id.btn_setting:
			index = 2;
			break;
		case R.id.btn_myself:
			index = 3;
			break;
		default:
			break;
		}
		if(currentTabIndex != index){
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			if(currentTabIndex == 0){
				trx.remove(fragments[currentTabIndex]);
			}else{
				trx.hide(fragments[currentTabIndex]);
			}
			if (!fragments[index].isAdded()) {
//				trx.add(R.id.fragment_container, fragments[index]);
				trx.replace(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		
		mTabs[currentTabIndex].setSelected(false);
		// 把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		XmppConnection.getInstance().closeConnection();
		unregisterReceiver(chatreceiver);
	}
	
	class ChatRececiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, final Intent intent) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						String from = intent.getStringExtra("from");
						String ncontent= intent.getStringExtra("chatContent");
						String ntime= intent.getStringExtra("chatTime");
						String to = MustCommonUtils.getInstance().getCurrentUserName();
						LiLog.d("chatRecevier:"+from+","+ncontent+","+ntime);
						String sql = "select * from chatMessageTable where sender = ? and receiver = ?";
						Cursor cursor = db.rawQuery(sql, new String[]{to,from});
						String sql2 = "insert into chatMessageTable values(?,?,?,?,?)";
						String sql3 = "delete from chatMessageTable where time = ? and receiver = ?";
						if(cursor == null || cursor.getCount() < 20){
							db.execSQL(sql2,new String[]{to,from,ncontent,ntime,"0"});
						}else{
							while(cursor.moveToFirst()){
								String currT = cursor.getString(3);
								db.execSQL(sql3, new String[]{currT,from});
								break;
							}
							db.execSQL(sql2,new String[]{to,from,ncontent,ntime,"0"});
						}
					} catch (Exception e) {
						LiLog.d("chatrecevier exception:"+e.getMessage());
					}
				}
			}).start();
		}
		
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		LiLog.d("onRestart start ...");
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

	}
}	
