package com.ljy.mychat.broadcast;

import com.ljy.mychat.ui.MainActivity;
import com.ljy.mychat.utils.AddUtil;
import com.ljy.mychat.utils.LiLog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

public class InviteBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		LiLog.d("invite");
		String from = AddUtil.getInstance().getFrom();
		String to = AddUtil.getInstance().getTo();
		if (null != from && null != to) {
			try {
				String sql = "insert into invite values(?,?)";
				MainActivity.db.execSQL(sql, new String[] { from, to });
			} catch (Exception e) {
				LiLog.d("数据库异常");
				e.printStackTrace();
			}
		}
	}

}
