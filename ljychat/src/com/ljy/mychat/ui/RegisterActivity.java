package com.ljy.mychat.ui;

import java.io.UnsupportedEncodingException;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;

import com.ljy.ljychat.R;
import com.ljy.mychat.utils.HttpRequestCommonUtils;
import com.ljy.mychat.utils.JSONParseUtils;
import com.ljy.mychat.utils.MustCommonUtils;
import com.ljy.mychat.xmpp.XmppConnection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
	private EditText username;
	private EditText password;
	private EditText confirmpwd;
	private XMPPConnection conn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ljy_activity_register);
		
		username = (EditText) findViewById(R.id.register_username);
		password = (EditText) findViewById(R.id.register_password);
		confirmpwd = (EditText) findViewById(R.id.register_confirm_password);
	}
	
	
	public void sendRegister(View v){
		
		final String name = username.getText().toString();
		final String pwd = password.getText().toString();
		final String surepwd = confirmpwd.getText().toString();
		
		//注册前检查是否有网络，此处要设置uses-permission，否则报错
		if(!MustCommonUtils.getInstance().isNetworkConnected(RegisterActivity.this)){
			Toast.makeText(RegisterActivity.this, "无网络",Toast.LENGTH_SHORT).show();
			return;
		}
		if(!registerRule(name,pwd,surepwd)){
			return;
		}
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					
//					String uri = "http://10.20.73.17:8080/LijiangyongChatServer/register";
//					String result = HttpRequestCommonUtils.sendRegisterRequestByPost(uri, name, pwd);
////					String state2 = new String(result.getBytes("iso8859-1"),"UTF-8");
//					String state = JSONParseUtils.getStateOrCode(result, "state");
//					String code = JSONParseUtils.getStateOrCode(result, "code");
//					
//					if("".equals(state) || "".equals(code)){
//						hander.sendEmptyMessage(1);
//						return;
//					}
//					if("200".equals(code)){
//						if("success".equals(state)){
//							hander.sendEmptyMessage(2);
//						}
//						if("fail".equals(state)){
//							hander.sendEmptyMessage(3);
//						}
//					}else{
//						hander.sendEmptyMessage(4);
//					}
					if(null == conn){
						conn = XmppConnection.getInstance().getConnection();
					}
					IQ result = XmppConnection.getInstance().regist(name, pwd);
					
					if (result == null) {  
				        Log.e("RegistActivity", "No response from server.");  
				    } else if (result.getType() == IQ.Type.RESULT) {  
				    	hander.sendEmptyMessage(2);
				    } else if(result.getError().toString().equalsIgnoreCase("conflict(409)")){  
				        Log.e("RegistActivity", "IQ.Type.ERROR: "+ result.getError().toString());  
				        hander.sendEmptyMessage(3);
				    } else {  
				        Log.e("RegistActivity", "IQ.Type.ERROR: "+ result.getError().toString());  
				        hander.sendEmptyMessage(4);
				    }  	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	Handler hander = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			
			if(msg.what == 1){
				Toast.makeText(RegisterActivity.this, "服务端返回json格式错误", Toast.LENGTH_SHORT).show();
			}
			
			if(msg.what == 2){
				Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
				Intent data = new Intent();
				data.putExtra("registername", username.getText().toString());
				RegisterActivity.this.setResult(RESULT_OK, data);
				XmppConnection.getInstance().closeConnection();
				finish();
			}
			
			if(msg.what == 3){
				Toast.makeText(RegisterActivity.this, "用户名已存在，注册失败", Toast.LENGTH_SHORT).show();
			}
			
			if(msg.what == 4){
				Toast.makeText(RegisterActivity.this, "服务端异常，注册失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	private boolean registerRule(String name, String pwd, String surepwd) {
		
		if(TextUtils.isEmpty(name) || name.length() < 2){
			Toast.makeText(RegisterActivity.this, "用户名不能为空或者长度不能小于3", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(TextUtils.isEmpty(pwd) || pwd.length() < 3){
			Toast.makeText(RegisterActivity.this, "密码不能为空或者长度不能小于3", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(TextUtils.isEmpty(surepwd)){
			Toast.makeText(RegisterActivity.this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		
		if(!pwd.equals(surepwd)){
			Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	
}
