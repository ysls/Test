package com.example.administrator.test.recever;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.administrator.test.R;
import com.example.administrator.test.service.LocationService;
import com.example.administrator.test.service.VoiceService;
import com.example.administrator.test.takephoto.CameraActivity;
import com.example.administrator.test.util.SPUtils;

import static com.example.administrator.test.MyApplication.*;
public class SmsReceiver extends BroadcastReceiver {
	private static final String TAG = "SmsReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG,"短信到来了");
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		//获取超级管理员
		DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		String GPS = SPUtils.getInstance().getString(SECRET_GPS,"#GPSLOCATION#");
        String ALARM = SPUtils.getInstance().getString(SECRET_ALARM,"#ALARMMUSICE#");
        String WIPE = SPUtils.getInstance().getString(SECRET_WIPE,"#WIPEDATA#");
        String LOCK = SPUtils.getInstance().getString(SECRET_LOCK,"#LOCKSCREEEN#");
		String PHOTO = SPUtils.getInstance().getString(SECRET_PHOTO,"#TAKEPHOTO#");
		String VOICE = SPUtils.getInstance().getString(SECRET_VOICE,"#RECORD#");
		for(Object obj:objs){
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
			String sender = smsMessage.getOriginatingAddress().substring(3);
			String body = smsMessage.getMessageBody();
			Log.i(TAG,body);
            Log.i(TAG,sender);
			if (!sender.equals(SPUtils.getInstance().getString(PREF_PHONE_NUMBER))){
				return;
            }
            if (!SPUtils.getInstance().getBoolean(PREF_IS_PROTECT)){
				SmsManager sm = SmsManager.getDefault();
				String phone = SPUtils.getInstance().getString(PREF_PHONE_NUMBER);
				sm.sendTextMessage(phone, null, "对不起，对方并未开启手机安全防护...", null, null);
			}
			if(body.contains(GPS)){
				Log.i(TAG,"返回位置信息.");
				//获取位置 放在服务里面去实现。
				Intent service = new Intent(context,LocationService.class);
				context.startService(service);
				abortBroadcast();
			}else if(body.contains(ALARM)){
				Log.i(TAG,"播放报警音乐.");
				MediaPlayer player = MediaPlayer.create(context, R.raw.music);
				player.setVolume(1.0f, 1.0f);
				player.start();
				abortBroadcast();
			}else if(body.contains(WIPE)){
				Log.i(TAG,"远程清除数据.");
				dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
				abortBroadcast();
			}else if(body.contains(LOCK)){
				Log.i(TAG,"远程锁屏.");
				try{
                    dpm.resetPassword("1111", 0);
                    dpm.lockNow();
                }catch (SecurityException e){
                    Log.i("onReceive: ",e.getMessage());
                }
				abortBroadcast();
			}else if(body.contains(PHOTO)){
                Log.i(TAG,"远程拍照.");
                Intent cameraIntent = new Intent(context, CameraActivity.class);
                cameraIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(cameraIntent);
                abortBroadcast();
            }else if(body.contains(VOICE)){
                Log.i(TAG,"远程录音.");
                Intent voiceService = new Intent(context, VoiceService.class);
                context.startService(voiceService);
                abortBroadcast();
            }
		}
	}
}
