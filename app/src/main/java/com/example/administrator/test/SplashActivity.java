package com.example.administrator.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import com.example.administrator.test.login.LoginActivity;
import com.example.administrator.test.util.SPUtils;


public class SplashActivity extends Activity {

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Intent intent;
			if (!TextUtils.isEmpty(SPUtils.getInstance().getString(MyApplication.TOKEN))){
				intent = new Intent(getApplicationContext(), MainActivity.class);
			}else {
				intent = new Intent(getApplicationContext(), LoginActivity.class);
			}
			startActivity(intent);
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_spalsh);
		// 动画
		ImageView img_bg_anim = (ImageView) findViewById(R.id.img_bg_anim);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(2000);
		alphaAnimation.setFillAfter(true);
		img_bg_anim.setAnimation(alphaAnimation);
		initData();
	}

	void initData() {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
			}
		}).start();
	}

}
