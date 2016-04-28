package com.ljy.mychat.xmpp;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.database.sqlite.SQLiteDatabase;

import com.ljy.mychat.ui.SplashActivity;
import com.ljy.mychat.utils.AddUtil;
import com.ljy.mychat.utils.LiLog;
import com.ljy.mychat.utils.MustCommonUtils;



public class XmppPresenceListener implements PacketListener {
	

	//监听好友的各种状态，和是否同意申请好友的状态
	@Override
	public void processPacket(Packet packet) {
		Presence presence = (Presence)packet;
		String from = presence.getFrom();
		String to = presence.getTo();
		LiLog.d("XmppPresenceListener,FROM:"+from);
		LiLog.d("XmppPresenceListener,to:"+presence.getTo());
		LiLog.d("presence states:"+presence.getType());
//		
//		InitApplication ia = new InitApplication();
//		ia.sendBroadcast(new Intent("com.invite"));
		
		if(presence.getType().equals(Presence.Type.available)){
			LiLog.d("status:available");
		}
		if(presence.getType().equals(Presence.Type.error)){
			LiLog.d("error");
		}
		if(presence.getType().equals(Presence.Type.subscribe)){
			//申请加为好友
			if(!MustCommonUtils.getInstance().getCurrentUserName().equals(from)){
				if(AddUtil.getInstance().getFrom() == null 
						|| ! AddUtil.getInstance().getFrom().equals(from)){
					AddUtil.getInstance().setFrom(from);
					AddUtil.getInstance().setTo(to);
//					InitApplication ia = new InitApplication();
//					ia.sendBroadcast2("com.invite");
					LiLog.d("from:addutil"+AddUtil.getInstance().getFrom());
					LiLog.d("to:addutil"+AddUtil.getInstance().getTo());
					try {
//						context.sendBroadcast(new Intent("com.invite"));
					} catch (Exception e) {
						e.printStackTrace();
						LiLog.d("sendBroadcast error");
					}
				}
				LiLog.d("对方请求加我为好友");
			}
		}
		if(presence.getType().equals(Presence.Type.subscribed)){
			LiLog.d("对方同意加我为好友");
		}
		if(presence.getType().equals(Presence.Type.unavailable)){
			LiLog.d("unavailable");
		}
		if(presence.getType().equals(Presence.Type.unsubscribed)){
			LiLog.d("unsubscribed");
		}
		if(presence.getType().equals(Presence.Type.unsubscribe)){
			LiLog.d("unsubscribe");
		}
		

	}

//	@Override
//	public void processPacket(Packet packet) {
//		Presence presence = (Presence) packet;
//		if(Constants.IS_DEBUG)
//		Log.e("xmppchat come", presence.toXML());
//		
//		String jid = presence.getFrom();//���ͷ�  
//        String to = presence.getTo();//���շ�  
//        //Presence.Type��7��״̬  
//        if (presence.getType().equals(Presence.Type.subscribe)) {// �յ���������
//        	if (!XmppConnection.getInstance().getFriendList().contains(new Friend(XmppConnection.getUsername(jid)))) {
//				Friend friend  = new Friend(XmppConnection.getUsername(jid));
//				friend.type = ItemType.from;
//				XmppConnection.getInstance().getFriendList().add(friend);
//			}
//        	
//			for (Friend friend : XmppConnection.getInstance().getFriendList()) {
//				System.out.println("�Һ���"+friend.username+"���ҵĹ�ϵ"+friend.type);
//				if (friend.equals(new Friend(XmppConnection.getUsername(jid))) && friend.type == ItemType.to) {
//					String userName = XmppConnection.getUsername(jid);
//					MyAndroidUtil.showNoti(userName+"ͬ����Ӻ���");
//					ChatItem msg =  new ChatItem(ChatItem.CHAT,userName,userName, "", userName+"ͬ����Ӻ���", DateUtil.now_MM_dd_HH_mm_ss(), 0);
//					NewMsgDbHelper.getInstance(MyApplication.getInstance()).saveNewMsg(userName);
//					MsgDbHelper.getInstance(MyApplication.getInstance()).saveChatMsg(msg);
//					MyApplication.getInstance().sendBroadcast(new Intent("ChatNewMsg"));
//					XmppConnection.getInstance().changeFriend(friend, ItemType.both);
//			        Log.e("friend", to+"���յ���������toBoth");
//				}
//				else if (friend.username.equals(XmppConnection.getUsername(jid))) {
//					XmppConnection.getInstance().changeFriend(friend, ItemType.from);
//			        Log.e("friend", to+"���յ���������toFrom");
//					MyAndroidUtil.showNoti(friend.username+"�������");
//			        NewFriendDbHelper.getInstance(MyApplication.getInstance()).saveNewFriend(XmppConnection.getUsername(jid));
//				}
//			}
//			MyApplication.getInstance().sendBroadcast(new Intent("friendChange"));
//		} 
//		else if (presence.getType().equals(Presence.Type.subscribed)) {// ͬ����Ӻ��ѣ���֪��Ϊʲô���Զ�ͬ��
//			if(Constants.IS_DEBUG)
//        	Log.e("friend", jid+"ͬ�����");
//		} 
//		else if (presence.getType().equals(Presence.Type.unsubscribe) ||presence.getType().equals(Presence.Type.unsubscribed)) {// �ܾ���Ӻ��� ɾ������
//			if(Constants.IS_DEBUG)
//    		Log.e("friend", "�ұ�����"+jid+"ɾ��");
//			for (Friend friend : XmppConnection.getInstance().getFriendList()) {
//				if (friend.equals(new Friend(XmppConnection.getUsername(jid)))) {
//					XmppConnection.getInstance().changeFriend(friend, ItemType.remove);
//				}
//			}
//			MyApplication.getInstance().sendBroadcast(new Intent("friendChange"));
//		}
//	}
	
}
