package com.example.administrator.test;

import android.app.Application;
import android.content.Intent;
import android.telephony.TelephonyManager;
import com.example.administrator.test.service.BlackNumberService;
import com.example.administrator.test.util.SPUtils;

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
    //流量
    public static final String PRE_TRAFFIC = "traffic";

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initBlackNumberService();
    }
    public static MyApplication getApplication(){
        return application;
    }

    private void initBlackNumberService(){
        Intent service = new Intent(this,
                BlackNumberService.class);
        startService(service);
    }
}
