package com.ljy.mychat.ui;

import com.ljy.ljychat.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MoneyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ljy_activity_money);
	}
	
	public void back(View v){
		finish();
	}
}
