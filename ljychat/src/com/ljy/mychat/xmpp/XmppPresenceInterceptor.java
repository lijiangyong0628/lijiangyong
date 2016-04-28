package com.ljy.mychat.xmpp;

import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import com.ljy.mychat.utils.LiLog;

public class XmppPresenceInterceptor implements PacketInterceptor {

	//我们自己的这边的申请好友状态等等
	@Override
	public void interceptPacket(Packet packet) {
		Presence presence = (Presence) packet;
		String from = presence.getFrom();
		String to = presence.getTo();
		LiLog.d("XmppPresenceInterceptor,FROM:"+from);
		LiLog.d("XmppPresenceInterceptor,to:"+presence.getTo());
		if(presence.getType().equals(Presence.Type.available)){
			LiLog.d("myavailable");
		}
		if(presence.getType().equals(Presence.Type.error)){
			LiLog.d("myerror");
		}
		if(presence.getType().equals(Presence.Type.subscribe)){
			LiLog.d("我:"+from+"添加"+to+"别人为好友");
		}
		if(presence.getType().equals(Presence.Type.subscribed)){
			LiLog.d("我同意别人的好友申请");
			Presence p = new Presence(Presence.Type.subscribed);
			p.setTo(from);
			XmppConnection.getInstance().getConnection().sendPacket(p);
		}
		if(presence.getType().equals(Presence.Type.unavailable)){
			LiLog.d("unavailable2");
		}
		if(presence.getType().equals(Presence.Type.unsubscribed)){
			LiLog.d("unsubscribed2");
		}
		if(presence.getType().equals(Presence.Type.unsubscribe)){
			LiLog.d("unsubscribe2");
		}
		
	}

//	@Override
//	public void interceptPacket(Packet packet) {
//		Presence presence = (Presence) packet;
//		if(Constants.IS_DEBUG)
//		Log.e("xmppchat send", presence.toXML());
//
//		String from = presence.getFrom();// ���ͷ�
//		String to = presence.getTo();// ���շ�
//		// Presence.Type��7��״̬
//		if (presence.getType().equals(Presence.Type.subscribe)) {// ������������
//			for (Friend friend : XmppConnection.getInstance().getFriendList()) {
//				System.out.println("�Һ���"+friend.username+"���ҵĹ�ϵ"+friend.type);
//				if ((friend.username.equals(XmppConnection.getUsername(to)) && friend.type == ItemType.from)) {
//					XmppConnection.getInstance().changeFriend(friend, ItemType.both);
//			        Log.e("friend", "����"+to+"������������toBoth");
//				}
//				else if (friend.username.equals(XmppConnection.getUsername(to))) {
//					XmppConnection.getInstance().changeFriend(friend, ItemType.to);
//			        Log.e("friend", "����"+to+"������������toTo");
//				}
//			}
//			
//			if (!XmppConnection.getInstance().getFriendList().contains(new Friend(XmppConnection.getUsername(to)))) {
//				Friend friend  = new Friend(XmppConnection.getUsername(to));
//				friend.type = ItemType.to;
//				XmppConnection.getInstance().getFriendList().add(friend);
//			}
//			MyApplication.getInstance().sendBroadcast(new Intent("friendChange"));
//		} 
//		else if (presence.getType().equals(Presence.Type.subscribed)) {// ͬ����Ӻ���
//			if(Constants.IS_DEBUG)
//	        Log.e("friend", to+"��ͬ��������");
////			MyApplication.getInstance().sendBroadcast(new Intent("friendChange"));
//		} 
//		else if (presence.getType().equals(Presence.Type.unsubscribe)||presence.getType().equals(Presence.Type.unsubscribed)) {// �ܾ���Ӻ��� ɾ������
//			if(Constants.IS_DEBUG)
//		       Log.e("friend", "��ɾ������"+to);
//			for (Friend friend : XmppConnection.getInstance().getFriendList()) {
//				if (friend.username.equals(XmppConnection.getUsername(to))) {
//					XmppConnection.getInstance().changeFriend(friend, ItemType.remove);
//				}
//			}
//			MyApplication.getInstance().sendBroadcast(new Intent("friendChange"));
//		}
//	}
}
