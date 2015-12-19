package com.ljy.mychat.utils;


public class MyConstants {

	//常量
	public final static boolean IS_DEBUG = true;
	public final static String SERVER_HOST = "10.20.73.17";
	public final static String SERVER_URL = "http://"+SERVER_HOST+":9090/plugins/xmppservice/";
	public static String SERVER_NAME = "10.20.73.17";
	public final static int SERVER_PORT = 5222;
	public final static int UPDATE_TIME =  60*1000;   //好友位置刷新时间，同时也是自己的位置上传时间间隔
	public final static int ADR_UPDATE_TIME =  30*1000;   //刷新自己的位置
	public final static String SHARED_PREFERENCES = "openfile";
	public final static String LOGIN_CHECK = "check";
	public final static String LOGIN_ACCOUNT = "account";
	public final static String LOGIN_PWD = "pwd";
	
	//URL
	public final static String URL_EXIT_ROOM = SERVER_URL+"exitroom";
	public final static String URL_EXIST_ROOM = SERVER_URL+"existroom";
	public final static String URL_GET_ADR = SERVER_URL+"getadr";
	public static String USR_NAME = "";
	public static String USR_PWD = "";
	
	//变量
	public static String USER_NAME = "";
	public static String PWD = "";
	public static com.ljy.mychat.model.User loginUser;
}
