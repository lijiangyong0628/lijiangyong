package com.ljy.mychat.xmpp;

import com.ljy.mychat.utils.LiLog;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

public class InitApplication extends Application {

	public static SQLiteDatabase db;
	
	@Override
	public void onCreate() {
		super.onCreate();
		db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getAbsolutePath()
				+"/invitefriend.db3", null);
		try{
			//申请好友表
			String sql = "create table InviteTable(inviteman varchar(155) primary key,acceptman varchar(155))";
			db.execSQL(sql);
		}catch(Exception e){
			e.printStackTrace();
			LiLog.d("InviteTable表已经存在");
		}
		try{
			//聊天信息表
			String sql2 = "create table chatMessageTable(sender varchar(255),"
					+ "receiver varchar(255),content varchar(555),time varchar(255),"
					+ "isSender varchar(10))";
			db.execSQL(sql2);
		}catch(Exception e){
			e.printStackTrace();
			LiLog.d("chatMessageTable表已经存在");
		}
		try{
			//会话列表
			String sql3 = "create table conversation(sender varchar(255),"
					+ "receiver varchar(255) primary key,content varchar(555),time varchar(255))";
			db.execSQL(sql3);
		}catch(Exception e){
			e.printStackTrace();
			LiLog.d("conversation表已经存在");
		}
	}
		
}
