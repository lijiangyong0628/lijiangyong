package com.ljy.mychat.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ljy.ljychat.R;
import com.ljy.mychat.utils.LiLog;
import com.ljy.mychat.utils.MustCommonUtils;
import com.ljy.mychat.utils.PreferenceManager;
import com.ljy.mychat.widget.ljySwitchButton;

public class SettingFragment extends Fragment implements OnClickListener{

		/**
		 * 设置新消息通知布局
		 */
		private RelativeLayout rl_switch_notification;
		/**
		 * 设置声音布局
		 */
		private RelativeLayout rl_switch_sound;
		/**
		 * 设置震动布局
		 */
		private RelativeLayout rl_switch_vibrate;
		/**
		 * 设置扬声器布局
		 */
		private RelativeLayout rl_switch_speaker;


		/**
		 * 声音和震动中间的那条线
		 */
		private TextView textview1, textview2;

		private LinearLayout blacklistContainer;
		
		private LinearLayout userProfileContainer;
		
		/**
		 * 退出按钮
		 */
		private Button logoutBtn;

		private ljySwitchButton notifiSwitch,soundSwitch,vibrateSwitch,speakerSwitch;
	 
		/**
		 * 诊断
		 */
		private LinearLayout llDiagnose;
		
		private PreferenceManager pm;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			LiLog.d("settintfragment:onCreateView");
			return inflater.inflate(R.layout.ljy_fragment_setting, container, false);
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			LiLog.d("settintfragment:onActivityCreated");
			if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)){
	            return;
	        }
			
			pm = PreferenceManager.getInstance(getActivity());
			
			rl_switch_notification = (RelativeLayout) getView().findViewById(R.id.rl_switch_notification);
			rl_switch_sound = (RelativeLayout) getView().findViewById(R.id.rl_switch_sound);
			rl_switch_vibrate = (RelativeLayout) getView().findViewById(R.id.rl_switch_vibrate);
			rl_switch_speaker = (RelativeLayout) getView().findViewById(R.id.rl_switch_speaker);

			
			notifiSwitch = (ljySwitchButton) getView().findViewById(R.id.switch_notification);
			soundSwitch = (ljySwitchButton) getView().findViewById(R.id.switch_sound);
			vibrateSwitch = (ljySwitchButton) getView().findViewById(R.id.switch_vibrate);
			speakerSwitch = (ljySwitchButton) getView().findViewById(R.id.switch_speaker);
			
			logoutBtn = (Button) getView().findViewById(R.id.btn_logout);
			if(!TextUtils.isEmpty(MustCommonUtils.getInstance().getCurrentUserName())){
				logoutBtn.setText(getString(R.string.button_logout) + "(" + 
						MustCommonUtils.getInstance().getCurrentUserName() + ")");
			}

			textview1 = (TextView) getView().findViewById(R.id.textview1);
			textview2 = (TextView) getView().findViewById(R.id.textview2);
			
			blacklistContainer = (LinearLayout) getView().findViewById(R.id.ll_black_list);
			userProfileContainer = (LinearLayout) getView().findViewById(R.id.ll_user_profile);
			llDiagnose=(LinearLayout) getView().findViewById(R.id.ll_diagnose);
			
			blacklistContainer.setOnClickListener(this);
			userProfileContainer.setOnClickListener(this);
			rl_switch_notification.setOnClickListener(this);
			rl_switch_sound.setOnClickListener(this);
			rl_switch_vibrate.setOnClickListener(this);
			rl_switch_speaker.setOnClickListener(this);
			logoutBtn.setOnClickListener(this);
			llDiagnose.setOnClickListener(this);
			
			
			// 震动和声音总开关,是否允许此开关打开
			// the vibrate and sound notification are allowed or not?
			if (pm.getSettingMsgNotification()) {
				notifiSwitch.openSwitch();
				
				// 是否打开声音
				// sound notification is switched on or not?
				if (pm.getSettingMsgSound()) {
				    soundSwitch.openSwitch();
				} else {
				    soundSwitch.closeSwitch();
				}
				
				// 是否打开震动
				// vibrate notification is switched on or not?
				if (pm.getSettingMsgVibrate()) {
				    vibrateSwitch.openSwitch();
				} else {
				    vibrateSwitch.closeSwitch();
				}

			} else {
			    notifiSwitch.closeSwitch();
				rl_switch_sound.setVisibility(View.GONE);
				rl_switch_vibrate.setVisibility(View.GONE);
				textview1.setVisibility(View.GONE);
				textview2.setVisibility(View.GONE);
			}
			
			// 是否打开扬声器
			// the speaker is switched on or not?
			if (pm.getSettingMsgSpeaker()) {
			    speakerSwitch.openSwitch();
			} else {
			    speakerSwitch.closeSwitch();
			}

		}

		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rl_switch_notification:
				if (notifiSwitch.isSwitchOpen()) {
				    notifiSwitch.closeSwitch();
					rl_switch_sound.setVisibility(View.GONE);
					rl_switch_vibrate.setVisibility(View.GONE);
					textview1.setVisibility(View.GONE);
					textview2.setVisibility(View.GONE);

					pm.setSettingMsgNotification(false);
				} else {
				    notifiSwitch.openSwitch();
					rl_switch_sound.setVisibility(View.VISIBLE);
					rl_switch_vibrate.setVisibility(View.VISIBLE);
					textview1.setVisibility(View.VISIBLE);
					textview2.setVisibility(View.VISIBLE);
					pm.setSettingMsgNotification(true);
				}
				break;
			case R.id.rl_switch_sound:
				if (soundSwitch.isSwitchOpen()) {
				    soundSwitch.closeSwitch();
				    pm.setSettingMsgSound(false);
				} else {
				    soundSwitch.openSwitch();
				    pm.setSettingMsgSound(true);
				}
				break;
			case R.id.rl_switch_vibrate:
				if (vibrateSwitch.isSwitchOpen()) {
				    vibrateSwitch.closeSwitch();
				    pm.setSettingMsgVibrate(false);
				} else {
				    vibrateSwitch.openSwitch();
				    pm.setSettingMsgVibrate(true);
				}
				break;
			case R.id.rl_switch_speaker:
				if (speakerSwitch.isSwitchOpen()) {
				    speakerSwitch.closeSwitch();
				    pm.setSettingMsgSpeaker(false);
				} else {
				    speakerSwitch.openSwitch();
				    pm.setSettingMsgVibrate(true);
				}
				break;
			case R.id.btn_logout: //退出登陆
				logout();
				break;
			case R.id.ll_black_list:
//				startActivity(new Intent(getActivity(), BlacklistActivity.class));
				break;
			case R.id.ll_diagnose:
//				startActivity(new Intent(getActivity(), DiagnoseActivity.class));
				break;
			case R.id.ll_user_profile:
//				startActivity(new Intent(getActivity(), UserProfileActivity.class).putExtra("setting", true)
//				        .putExtra("username", EMChatManager.getInstance().getCurrentUser()));
				break;
			default:
				break;
			}
			
		}

		void logout() {
			LiLog.d("logout to start ...");
			final ProgressDialog pd = new ProgressDialog(getActivity());
			String st = "正在退出登录";
			pd.setMessage(st);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
			Intent intent = new Intent(getActivity(),LoginActivity.class);
			startActivity(intent);
			getActivity().finish();
			LiLog.d("logout to end ...");
		}

		
	    @Override 
	    public void onSaveInstanceState(Bundle outState) {
	    	super.onSaveInstanceState(outState);
	    	LiLog.d("onSaveInstanceState to start ...");
	    	LiLog.d("onSaveInstanceState to end ...");
	    }

	
	
	
}