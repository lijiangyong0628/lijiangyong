package com.ljy.mychat.model;

import java.io.Serializable;

import android.graphics.Bitmap;


public class ChatItem implements Serializable{
	public static final int CHAT = 0; 
	public static final int GROUP_CHAT = 1;
	public static final int NOTI = 2;
	
	public int chatType;   // 0 chat  1 groupChat 2 noti
	public String chatName;  //Ⱥ�ĵĻ���username��һ��
	public String username;  //�Է����ǳ�
	public String head;
	public String msg;
	public String sendDate;
	public int inOrOut; //0����in 1����out
	public String whos;
	
	//ѡ��
	public Bitmap headBitmap;

	public ChatItem() {
		super();
	}

	public ChatItem(int chatType,String chatName,String username, String head, String msg, String sendDate,
			int inOrOut) {
		super();
		this.chatType = chatType;
		this.chatName = chatName;
		this.username = username;
		this.head = head;
		this.msg = msg;
		this.sendDate = sendDate;
		this.inOrOut = inOrOut;
	}
}
