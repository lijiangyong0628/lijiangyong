package com.ljy.mychat.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

public class MustCommonUtils {

	private String currentUsername;
	private static MustCommonUtils instance;
	/*
	 * 检查是否有网络
	 */
	public boolean isNetworkConnected(Context context){
		if(null != context){
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			if(null != networkInfo){
				return networkInfo.isAvailable();
			}
		}
		return false;
	}
	
	public boolean isSDCardExisted(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}
	
	private MustCommonUtils(){

	}
	
	public static MustCommonUtils getInstance(){
		if(null == instance){
			instance = new MustCommonUtils();
		}
		return instance;
	}
	
	public void setCurrentUserName(String currentUsername){
		this.currentUsername = currentUsername;
	}
	
	public String getCurrentUserName(){
		return currentUsername;
	}
	
	
}
