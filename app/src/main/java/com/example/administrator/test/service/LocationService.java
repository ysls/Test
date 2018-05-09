package com.example.administrator.test.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.administrator.test.util.SPUtils;

import static com.example.administrator.test.MyApplication.PREF_PHONE_NUMBER;

/**
 * 地理位置服务
 * @author 文江
 *
 */
public class LocationService extends Service {
	public LocationClient mLocationClient = null;
	public BDAbstractLocationListener myListener = new MyLocationListener();
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
//        option.setScanSpan(5*1000);
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
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            Log.i("onReceiveLocation: " ,latitude + "   " + longitude);
            StringBuilder builder = new StringBuilder("");
            builder.append(location.getTime()).append(",经度：").append(longitude)
                    .append(",纬度：").append(latitude)
                    .append(",地址：").append(location.getAddrStr());
            String all = builder.toString();
            Log.i("onReceiveLocation: ",all);
            SmsManager sm = SmsManager.getDefault();

            String phone = SPUtils.getInstance().getString(PREF_PHONE_NUMBER);
            if (!TextUtils.isEmpty(phone)) {
                sm.sendTextMessage(phone, null, all, null, null);
            }
        }


    }

}
