package com.example.administrator.test.recever;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.example.administrator.test.util.SPUtils;

import static com.example.administrator.test.MyApplication.PREF_BIND_SIM;
import static com.example.administrator.test.MyApplication.PREF_IS_PROTECT;
import static com.example.administrator.test.MyApplication.PREF_PHONE_NUMBER;
public class BootCompleteRecevier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isProtect = SPUtils.getInstance().getBoolean(PREF_IS_PROTECT);
        String serialNumber = SPUtils.getInstance().getString(PREF_BIND_SIM);
        if(isProtect) {
            if (!TextUtils.isEmpty(serialNumber)) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String simSerialNumber = tm.getSimSerialNumber();
                if (serialNumber.equals(simSerialNumber)) {
                    Log.e("Boot", "手机安全");
                } else {
                    Log.e("Boot", "SIM卡发生变化，危险");
                    //发送警报短信
                    SmsManager smsManager = SmsManager.getDefault();//短信管理器
                    String phone = SPUtils.getInstance().getString(PREF_PHONE_NUMBER);
                    smsManager.sendTextMessage(phone,null,"SIM卡发生变化",null,null);
                }
            }
        }
    }
}
