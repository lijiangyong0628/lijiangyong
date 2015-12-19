package com.ljy.mychat.xmpp;

import org.jivesoftware.smack.ConnectionListener;

import com.ljy.mychat.utils.LiLog;


public class XmppConnectionListener implements ConnectionListener{

	@Override
	public void connectionClosed() {
		XmppConnection.getInstance().setNull();
		LiLog.d("connectionClosed");
	}

	@Override
	public void connectionClosedOnError(Exception arg0) {
		LiLog.d("connectionClosedOnError");
	}

	@Override
	public void reconnectingIn(int arg0) {
		LiLog.d("reconnectingIn");
	}

	@Override
	public void reconnectionFailed(Exception arg0) {
		LiLog.d("reconnectionFailed");
	}

	@Override
	public void reconnectionSuccessful() {
		LiLog.d("reconnectionSuccessful");
	}

}
