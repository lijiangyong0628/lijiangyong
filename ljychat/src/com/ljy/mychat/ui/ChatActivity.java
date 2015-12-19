package com.ljy.mychat.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ljy.ljychat.R;
import com.ljy.mychat.model.ChatItem;
import com.ljy.mychat.utils.ExpressionUtil;
import com.ljy.mychat.utils.LiLog;
import com.ljy.mychat.utils.MustCommonUtils;
import com.ljy.mychat.utils.MyUtils;
import com.ljy.mychat.widget.TitleBar;
import com.ljy.mychat.xmpp.XmppConnection;

public class ChatActivity extends Activity {
	
	private TitleBar titlebar;
	private EditText chat_edit;
	private GridView emothionGridview;
	private SwipeRefreshLayout swipeFreshLayout;
	private ListView chat_listview;
	private ImageButton emotion_imagebutton;
	private ImageButton voice_chat_imagebutton;
	private Button extras_chat_imagebutton;
	private int[] image;
	private ArrayList<String> chatContent = new ArrayList<String>();
	private ArrayList<String> chatTime = new ArrayList<String>();
	private ArrayList<Integer> chat_flag = new ArrayList<Integer>();
	private String chat_name;
	private ChatBroadcast cb;
	private SQLiteDatabase db;
	private String my_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ljy_activity_chatmessage);
		db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getAbsolutePath()
				+"/invitefriend.db3", null);
		initWidget();
		Intent intent = getIntent();
		chat_name = intent.getStringExtra("chat_name");
		my_name = MustCommonUtils.getInstance().getCurrentUserName();
		titlebar.setTitle(chat_name);
		titlebar.getLeftLayout().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		chat_edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		image = new int[]{R.drawable.ee_1,R.drawable.ee_2,R.drawable.ee_3,R.drawable.ee_4,
				R.drawable.ee_5,R.drawable.ee_6,R.drawable.ee_7,R.drawable.ee_8,
				R.drawable.ee_9,R.drawable.ee_10,R.drawable.ee_11,R.drawable.ee_12,
				R.drawable.ee_13,R.drawable.ee_14,R.drawable.ee_15,R.drawable.ee_16,
				R.drawable.ee_17,R.drawable.ee_18,R.drawable.ee_19,R.drawable.ee_20,
				R.drawable.ee_21,R.drawable.ee_22,R.drawable.ee_23,R.drawable.ee_24,
				R.drawable.ee_25,R.drawable.ee_26,R.drawable.ee_27,R.drawable.ee_28,
				R.drawable.ee_29,R.drawable.ee_30,R.drawable.ee_31,R.drawable.ee_32,
				R.drawable.ee_33,R.drawable.ee_34,R.drawable.ee_35
		};
		
		emothionGridview.setAdapter(adapter);
		emothionGridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				Drawable drawable = getResources().getDrawable(image[position]);
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
				String imgStr = "[emothion:"+position+"]";
				SpannableString spannable = new SpannableString(imgStr);
				ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
				spannable.setSpan(span, 0, imgStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				int where = chat_edit.getSelectionStart();
				chat_edit.getText().insert(where, spannable);
			}
		});
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					String sql = "select * from chatMessageTable where sender = ? and receiver = ?";
					Cursor cursor = db.rawQuery(sql, new String[]{my_name,chat_name});
					if(cursor.getCount() == 0 || null == cursor){
						return;
					}
					while (cursor.moveToNext()) {
						chatContent.add(cursor.getString(2));
						chatTime.add(cursor.getString(3));
						chat_flag.add(Integer.valueOf(cursor.getString(4)));
					}
					handler.sendEmptyMessage(2);
				} catch (Exception e) {
					LiLog.d("select chatmessagetable exception:"+e.getMessage());
				}
			}
		}).start();
			
		cb = new ChatBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.li.chatbroadcast");
		registerReceiver(cb, filter);
	}
	
	private void initChatListView() {
		if(chatContent.size() == 0){
			return;
		}
		chat_listview.setAdapter(chatAdapter);
		chat_listview.setSelection(chatContent.size());
	}

	public void initWidget(){
		titlebar = (TitleBar) findViewById(R.id.chat_title_bar);
		chat_edit = (EditText) findViewById(R.id.chat_edit_text);
		emothionGridview = (GridView) findViewById(R.id.emothion_griwview);
		swipeFreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);
		chat_listview = (ListView) findViewById(R.id.chat_list);
		emotion_imagebutton = (ImageButton) findViewById(R.id.emotion_imagebutton);
		voice_chat_imagebutton = (ImageButton) findViewById(R.id.voice_chat);
		extras_chat_imagebutton = (Button) findViewById(R.id.extras_chat);
	}
	
	class ViewHolder{
		ImageView iv;
	}
	
	BaseAdapter adapter = new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if(convertView == null){
				vh = new ViewHolder();
				convertView = LayoutInflater.from(ChatActivity.this).inflate
						(R.layout.convertview_emothion, null);
				vh.iv = (ImageView) convertView.findViewById(R.id.iv_expression);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
			vh.iv.setImageResource(image[position]);
			return convertView;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			return image[position];
		}
		
		@Override
		public int getCount() {
			return image.length;
		}
	};
	
	private static int flag = 0;
	public void onImageButtonClick(View v){
		int id = v.getId();
		switch (id) {
		case R.id.emotion_imagebutton:
			if(flag == 0){
				emotion_imagebutton.setImageResource(R.drawable.ljy_chatting_biaoqing_btn_enable);
				flag = 1;
				emothionGridview.setVisibility(View.VISIBLE);
			}else{
				emotion_imagebutton.setImageResource(R.drawable.ljy_chatting_biaoqing_btn_normal);
				flag = 0; 
				emothionGridview.setVisibility(View.GONE);
			}
			break;
		case R.id.voice_chat:
			
			break;
		case R.id.extras_chat:
			String content = chat_edit.getText().toString();
			String sendTime = MyUtils.getFormatTime(System.currentTimeMillis());
			if("".equals(content) || null == content){
				Toast.makeText(this, "写点什么吧", Toast.LENGTH_SHORT).show();
			}else{
				if(chatContent.size() == 20){
					chatContent.remove(0);
					chatTime.remove(0);
					chat_flag.remove(0);
				}
				chatContent.add(content);
				chatTime.add(sendTime);
				chat_flag.add(1);
				initChatListView();
				LiLog.d("content:"+content);
				chat_edit.setText("");
				//发送给好友消息
				try {
					XmppConnection.getInstance().sendMsg(chat_name, "{\"content\":\""+
				content+"\",\"time\":\""+sendTime+"\"}", ChatItem.CHAT);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}
	
	class ViewHolder2{
		TextView tv_receiver;
		TextView timeTv_recevier;
		TextView tv_send;
		TextView timeTv_send;
		LinearLayout ll_receiver;
		LinearLayout ll_send;
	}
	
	BaseAdapter chatAdapter = new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder2 vh = null;
			if(convertView == null){
				vh = new ViewHolder2();
				convertView = LayoutInflater.from(ChatActivity.this).inflate
						(R.layout.convertview_chatactivity_sendmessage, null);
				vh.timeTv_recevier = (TextView) convertView.findViewById(R.id.timestamp);
				vh.tv_receiver = (TextView) convertView.findViewById(R.id.tv_chatcontent);
				vh.timeTv_send = (TextView) convertView.findViewById(R.id.timestamp2);
				vh.tv_send = (TextView) convertView.findViewById(R.id.tv_chatcontent2);
				vh.ll_receiver = (LinearLayout) convertView.findViewById(R.id.recevier_chat);
				vh.ll_send = (LinearLayout) convertView.findViewById(R.id.send_chat);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder2) convertView.getTag();
			}
			if(chat_flag.get(position) == 1){
				vh.ll_receiver.setVisibility(View.GONE);
				vh.ll_send.setVisibility(View.VISIBLE);
				vh.timeTv_send.setText(chatTime.get(position));
				vh.tv_send.setText(ExpressionUtil.getText(ChatActivity.this, chatContent.get(position),"2"));
			}else{
				vh.ll_send.setVisibility(View.GONE);
				vh.ll_receiver.setVisibility(View.VISIBLE);
				vh.timeTv_recevier.setText(chatTime.get(position));
				vh.tv_receiver.setText(ExpressionUtil.getText(ChatActivity.this, chatContent.get(position),"2"));
			}
			return convertView;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			return chatContent.get(position);
		}
		
		@Override
		public int getCount() {
			return chatContent.size();
		}
	};
	
	String cc;
	String ct;
	class ChatBroadcast extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String from = intent.getStringExtra("from");
			if(from.equals(chat_name)){
				cc = intent.getStringExtra("chatContent");
				ct = intent.getStringExtra("chatTime");
				LiLog.d("cc:"+cc+",ct:"+ct);
				handler.sendEmptyMessage(1);
			}
		}
		
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1){
				if(chatContent.size() == 20){
					chatContent.remove(0);
					chatTime.remove(0);
					chat_flag.remove(0);
				}
				chatContent.add(cc);
				chatTime.add(ct);
				chat_flag.add(0);
				initChatListView();
			}
			if(msg.what == 2){
				initChatListView();
			}
		}
	};
	
	protected void onStop() {
		super.onStop();
		LiLog.d("onStop");
		new Thread(new Runnable() {
			@Override
			public void run() {
				String sql2 = "delete from chatMessageTable where sender = ? and receiver = ?";
				db.execSQL(sql2, new String[]{my_name,chat_name});
				String sql = "insert into chatMessageTable values(?,?,?,?,?)";
				for (int i = 0; i < chatContent.size(); i++) {
					db.execSQL(sql, new String[]{my_name,chat_name,chatContent.get(i)
					 ,chatTime.get(i),chat_flag.get(i)+""});
				}
				try {
					if(chatContent.size() == 0){
						return;
					}
					String sql3 = "insert into conversation values(?,?,?,?)";
					db.execSQL(sql3, new String[]{my_name,chat_name,chatContent.
							get(chatContent.size()-1),chatTime.get(chatContent.size()-1)});
				} catch (Exception e) {
					LiLog.d("insert into conversation exception:"+e.getMessage());
					String sql4= "update conversation set content = ?,time = ? where "
							+ "sender=? and receiver=?";
					db.execSQL(sql4, new String[]{chatContent.get(chatContent.size()-1),
							chatTime.get(chatContent.size()-1),my_name,chat_name});
				}
//				Intent intent_conversation = new Intent();
//				intent_conversation.setAction("com.li.conversation");
//				sendBroadcast(intent_conversation);
			}
		}).start();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LiLog.d("onDestroy");
		unregisterReceiver(cb);
//		db.close();
	}
}
