package com.example.administrator.test.recever;


import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;
import com.example.administrator.test.service.LocationService;
import com.example.administrator.test.util.SPUtils;

import static com.example.administrator.test.MyApplication.*;


/**
 * 短信广播广播监听
 * @author 文江
 *
 */
public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		 System.out.println(TAG+"短信到来了");
		Log.i(TAG,"短信到来了");
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		
		//获取超级管理员
		DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		String GPS = SPUtils.getInstance().getString(SECRET_GPS);
        String ALARM = SPUtils.getInstance().getString(SECRET_ALARM);
        String WIPE = SPUtils.getInstance().getString(SECRET_WIPE);
        String LOCK = SPUtils.getInstance().getString(SECRET_LOCK);
		
		for(Object obj:objs){
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
			String sender = smsMessage.getOriginatingAddress();
			String body = smsMessage.getMessageBody();
			if (!sender.equals(SPUtils.getInstance().getString(PREF_PHONE_NUMBER)) || !SPUtils.getInstance().getBoolean(PREF_IS_PROTECT)){
			    return;
            }
			if(body.contains(GPS)){
				Log.i(TAG,"返回位置信息.");
				//获取位置 放在服务里面去实现。
				Intent service = new Intent(context,LocationService.class);
				context.startService(service);
				abortBroadcast();
			}else if(body.contains(ALARM)){
				Log.i(TAG,"播放报警音乐.");
				MediaPlayer player = MediaPlayer.create(context, null);
				player.setVolume(1.0f, 1.0f);
				player.start();
				abortBroadcast();
			}else if(body.contains(WIPE)){
				Log.i(TAG,"远程清除数据.");
				dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
				abortBroadcast();
			}else if(body.contains(LOCK)){
				Log.i(TAG,"远程锁屏.");
				dpm.resetPassword("123", 0);
				dpm.lockNow();
				abortBroadcast();
			}
		}
	}

}
