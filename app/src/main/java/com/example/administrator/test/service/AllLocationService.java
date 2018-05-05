package com.example.administrator.test.service;


import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import com.example.administrator.test.login.CodeBean;
import com.example.administrator.test.network.RetrofitServiceManager;
import com.example.administrator.test.util.SPUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.administrator.test.MyApplication.PREF_PHONE_NUMBER;
import static com.example.administrator.test.MyApplication.TOKEN;

/**
 * 地理位置服务
 * @author 文江
 *
 */
public class AllLocationService extends Service {
	private LocationManager lm;
	private MyListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		listener = new MyListener();
		//criteria 查询条件
		//true只返回可用的位置提供者 
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//获取准确的位置。
		criteria.setCostAllowed(true);//允许产生开销
		String name = lm.getBestProvider(criteria, true);
		System.out.println("最好的位置提供者："+name);
		lm.requestLocationUpdates(name, 10*1000, 0, listener);
		super.onCreate();
	}
	
	private class MyListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			RetrofitServiceManager.getService().setPosition(SPUtils.getInstance().getString(TOKEN),location.getLatitude()+","+location.getLongitude())
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Subscriber<CodeBean>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {

						}

						@Override
						public void onNext(CodeBean codeBean) {
							Log.e("Position: ", codeBean.getMsg());
						}
					});
		}
		//当位置提供者 状态发生变化的时候调用的方法。
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		//当某个位置提供者 可用的时候调用的方法。
		@Override
		public void onProviderEnabled(String provider) {
			
		}
		//当某个位置提供者 不可用的时候调用的方法。
		@Override
		public void onProviderDisabled(String provider) {
			
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(listener);
		listener = null;
	}

}
