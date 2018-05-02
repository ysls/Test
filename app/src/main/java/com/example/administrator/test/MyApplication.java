package com.example.administrator.test;

import android.app.Application;
import android.telephony.TelephonyManager;
import com.example.administrator.test.util.SPUtils;


/**
 * Created by 梁遂 on 2018/3/27.
 */

public class MyApplication extends Application {
    private static MyApplication application = null;
    public static String TOKEN = "token";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_CONFIG = "config";
    public static final String PREF_BIND_SIM = "bind_sim";
    public static final String PREF_PHONE_NUMBER = "phone_number";
    public static final String PREF_IS_PROTECT = "is_protect";
    //密语
    public static final String SECRET_GPS = "gps";
    public static final String SECRET_ALARM = "alarm";
    public static final String SECRET_WIPE = "wipe";
    public static final String SECRET_LOCK = "lock";
    public static final String SECRET_VOICE = "voice";
    public static final String SECRET_PHOTO = "photo";

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
    public static MyApplication getApplication(){
        return application;
    }

    private void correctSIM(){
        //获取绑定的SIM卡号
        String sim = SPUtils.getInstance().getString("SIM");
        //获取当前的SIM卡号
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//        String nowSim = telephonyManager.getSimSerialNumber();
        //若两者不等，则发送短信告知SIM已被更换
//        if (!TextUtils.equals(sim,nowSim)){
//            SendMessage("手机卡已被更换");
//        }
    }
}
