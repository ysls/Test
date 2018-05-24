package com.example.administrator.test.util;

import android.content.Context;
import android.util.Log;
import com.example.administrator.test.backup.ContactsBean;
import com.example.administrator.test.backup.DownBean;
import com.example.administrator.test.login.CodeBean;
import com.example.administrator.test.network.RetrofitServiceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.ResponseBody;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.administrator.test.MyApplication.TOKEN;

public class BackUpUtils {

    public static void upload(Context context ,BackUpCallback callback){
        ArrayList<ContactsBean> arrayList = ContactsUtil.readContact(context);
        callback.preSmsBackup(arrayList.size());
        int progress = 1;
        if (arrayList.size() == 0){
            return;
        }
        //上传数据到后台
        upload(arrayList);
        for (ContactsBean hash:arrayList) {
            progress++;
            callback.onSmsBackup(progress);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void download(final Context context,final BackUpCallback callback){
        RetrofitServiceManager.getService().downContacts(SPUtils.getInstance().getString(TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<DownBean>() {
                    @Override
                    public void call(DownBean codeBean) {
                        // json转为带泛型的list
                        ArrayList<ContactsBean> retList = new Gson().fromJson(codeBean.getMlist(),
                                new TypeToken<ArrayList<ContactsBean>>() {
                                }.getType());
                        if (retList.size() == 0){
                            return;
                        }
                        callback.preSmsBackup(retList.size());
                        for (int i = 0; i < retList.size(); i++) {
                            ContactsUtil.addContact(context,retList.get(i).getName(),retList.get(i).getPhone());
                            Log.e("add: ", retList.get(i).getName());
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            callback.onSmsBackup(i+1);
                        }
                    }
                });


    }


    public interface BackUpCallback {
        /**
         * 备份联系人前的回调
         * @param totalCount 联系人总数
         */
        void preSmsBackup(int totalCount);

        /**
         * 备份过程中的回调
         * @param progress 备份进度(备份联系人条数)
         */
        void onSmsBackup(int progress);
    }

    private static void upload(ArrayList<ContactsBean> arrayList){
        RetrofitServiceManager.getService().upContacts(SPUtils.getInstance().getString(TOKEN),new Gson().toJson(arrayList))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<CodeBean>() {
                    @Override
                    public void call(CodeBean codeBean) {
                        Log.e( "call: ",codeBean.getMsg() );
                    }
                });
    }

}
