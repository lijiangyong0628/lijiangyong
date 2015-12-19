package com.ljy.mychat.xmpp;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.ljy.mychat.utils.JSONParseUtils;
import com.ljy.mychat.utils.LiLog;
import com.ljy.mychat.utils.MustCommonUtils;

public class XmppMessageListener implements PacketListener{
	
	private Context context;
	private SQLiteDatabase db;
	
	public XmppMessageListener(Context context) {
		this.context = context;
		db = SQLiteDatabase.openOrCreateDatabase(context.getFilesDir().getAbsolutePath()
				+"/invitefriend.db3", null);
	}

	//监听收到的消息
	@Override
	public void processPacket(Packet packet) {
		Message message = (Message) packet;
		LiLog.d("XmppMessageListener:body:"+message.getBody());
		String body = message.getBody();
		String from = XmppConnection.getInstance().getUsername(message.getFrom());
		String content = JSONParseUtils.getStateOrCode(body, "content");
		String time = JSONParseUtils.getStateOrCode(body, "time");
		Intent intent = new Intent();
		intent.setAction("com.li.chatbroadcast");
		intent.putExtra("chatContent", content);
		intent.putExtra("chatTime", time);
		intent.putExtra("from", from);
		context.sendBroadcast(intent);
		LiLog.d("content:"+content+",time:"+time);
		Intent intent2 = new Intent();
		intent2.setAction("com.li.chatrecevier");
		intent2.putExtra("chatContent", content);
		intent2.putExtra("chatTime", time);
		intent2.putExtra("from", from);
		context.sendBroadcast(intent2);
		LiLog.d("send message");
		
		try {
			String sql = "insert into conversation values(?,?,?,?)";
			db.execSQL(sql, new String[]{MustCommonUtils.getInstance().getCurrentUserName(),
					from,content,time});
		} catch (Exception e) {
			LiLog.d("insert into conversation exception:"+e.getMessage());
			String sql4= "update conversation set content = ?,time = ? where "
					+ "sender=? and receiver=?";
			db.execSQL(sql4, new String[]{content,time,MustCommonUtils.getInstance()
					.getCurrentUserName(),from});
		}
		
		Intent intent3 = new Intent();
		intent3.setAction("com.li.conversationlistupdate");
		context.sendBroadcast(intent3);
	}

}
