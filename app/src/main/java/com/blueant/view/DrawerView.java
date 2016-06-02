package com.blueant.view;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;

import login.comblueant.activity.LoginActivity;
import login.comblueant.teamwork.R;
/*import com.topnews.SettingsActivity;*/
/** 
 * �Զ���SlidingMenu �����˵���
 * */
public class DrawerView implements OnClickListener{
	private final Activity activity;
	SlidingMenu menu;
	private RelativeLayout changeAccount_btn;

	public DrawerView(Activity activity) {
		this.activity = activity;
	}

	public  SlidingMenu initSlidingMenu() {

		initView();
		return menu;
	}

	private void initView() {

		changeAccount_btn =(RelativeLayout)menu.findViewById(R.id.account_btn);
		changeAccount_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.account_btn:
			activity.startActivity(new Intent(activity,LoginActivity.class));
			activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;

		default:
			break;
		}
	}
}
