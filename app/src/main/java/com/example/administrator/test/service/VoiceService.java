package com.example.administrator.test.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class VoiceService extends Service {

	private MediaRecorder recorder;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// 当服务第一次开启的时候
	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("voice service i am coming");
		// 如何监听电话的状态
		// 电话类TelephonyManager:获取电话的一些服务或者状态

		// 1.获取电话管理者的实例
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		// 2.注册一个电话状态的监听
		tm.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);

	}

	// 监听电话的状态
	private class MyPhoneStateListener extends PhoneStateListener {
		private String path;

		// 当设备的状态发生改变的时候调用
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			// 具体判断一下电话是处于什么状态
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:// 空闲状态
				System.out.println("i am idle");
				if (recorder != null) {
					recorder.stop();
					recorder.reset();
					recorder.release(); // 释放资源

					// 上传到服务器
					if(!TextUtils.isEmpty(path)){
						// TODO: 2018/5/3 录音上传 
//						NewsUtils.upImage(path, getApplicationContext());
						
						System.out.println("录音结束！！！");
					}
					
				}
				break;

			case TelephonyManager.CALL_STATE_OFFHOOK:// 接听状态
				System.out.println("i am call in ");
				// 开始录音
				if (recorder != null) {
					recorder.start();
				} else {
					System.out.println("recoder null-------------------");
				}
				break;

			case TelephonyManager.CALL_STATE_RINGING:// 响铃状态
				// 准备一个录音机
				System.out.println("i am ringing ");
				recorder = new MediaRecorder();

				// 设置音频的来源 MediaRecorder.AudioSource.MIC（麦克风），只能录单方的
				// MediaRecorder.AudioSource.VOICE_CALL,双方通话

				recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);

				// 设置音频的输出格式：THREE_GPP
				recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

				// 设置音频的编码方式：
				// 微信的按住说话就是AMR_NB这种格式
				
				
				path = Environment.getExternalStorageDirectory().getAbsolutePath();  
				path+="puppyvoicetest.3gp";
				//path += "/" + new Date().getTime() + ".3gp";
				
				recorder.setOutputFile(path);
				recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				
				System.out.println(path);
				// 准备录音了
				try {
					recorder.prepare();
				} catch (Exception e) {
					throw new RuntimeException("录音失败");
				}

				break;
			default:
				break;
			}

			super.onCallStateChanged(state, incomingNumber);
		}
	}

	// 当服务销毁的时候
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	// 相当于activity的onStart（）方法
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

}
