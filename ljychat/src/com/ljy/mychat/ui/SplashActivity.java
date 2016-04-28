package com.ljy.mychat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.ljy.ljychat.R;

public class SplashActivity extends Activity {
	
	private RelativeLayout root;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ljy_activity_splash);
		super.onCreate(savedInstanceState);
		
		root=(RelativeLayout) findViewById(R.id.ljy_splash_root);
		
		//透明度动画
		AlphaAnimation animation=new AlphaAnimation(0.4f, 1.0f);
		animation.setDuration(1500);
		root.startAnimation(animation);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		//休眠不能放在主线程中会阻塞图片的加载，新开一个线程，这样就是让子线程阻塞
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					startActivity(new Intent(SplashActivity.this,LoginActivity.class));
					finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
}
