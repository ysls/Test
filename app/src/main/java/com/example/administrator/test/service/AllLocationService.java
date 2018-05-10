package com.example.administrator.test.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.administrator.test.login.CodeBean;
import com.example.administrator.test.network.RetrofitServiceManager;
import com.example.administrator.test.util.SPUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.example.administrator.test.MyApplication.PREF_PHONE_NUMBER;
import static com.example.administrator.test.MyApplication.TOKEN;

/**
 * 地理位置服务
 * @author 文江
 *
 */
public class AllLocationService extends Service {
	public LocationClient mLocationClient = null;
	public BDAbstractLocationListener myListener = new AllLocationService.MyLocationListener();
	Context context;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("GPS我进来啦。。");
		//定位
		location();

	}

	public void location() {
		mLocationClient = new LocationClient(this);
		// 声明LocationClient类
		mLocationClient.registerLocationListener(myListener);
		// 注册监听函数
		// 设置定位条件

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 是否打开GPS
		option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
        option.setScanSpan(60*1000);
		option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(true);
//        option.setAddrType("all");
		option.setLocationMode(com.baidu.location.LocationClientOption.LocationMode.Hight_Accuracy);
//		option.setProdName(getPackageName());

		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}


	public class MyLocationListener extends BDAbstractLocationListener {


		@Override
		public void onReceiveLocation(BDLocation location) {

		    if (location == null){
		        return;
            }
			double latitude = location.getLatitude();    //获取纬度信息
			double longitude = location.getLongitude();    //获取经度信息

			StringBuilder builder = new StringBuilder("");
			builder.append(longitude).append(",").append(latitude);
			String all = builder.toString();
			Log.e( "onReceiveLocation: ", all);
			RetrofitServiceManager.getService().setPosition(SPUtils.getInstance().getString(TOKEN),all)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(new Subscriber<CodeBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(CodeBean codeBean) {
                            Log.e("onNext: ",codeBean.getMsg() );
                        }
                    });

		}


	}

}
