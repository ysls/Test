package com.example.administrator.test.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

public class BackUpUtils {

    public static void upload(Context context ,BackUpCallback callback){
        ArrayList<HashMap<String,String>> arrayList = ContactsUtil.readContacts(context);
        callback.preSmsBackup(arrayList.size());
        int progress = 0;
        for (HashMap<String,String> hash:arrayList) {
            progress++;
            callback.onSmsBackup(progress);
            // TODO: 2018/5/2 上传联系人到后台
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void download(Context context,BackUpCallback callback){
        int progress = 0;
        callback.preSmsBackup(5);
        for (int i = 0; i < 5; i++) {
            ContactsUtil.addContact(context,"aa"+i,"18428326467");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            callback.onSmsBackup(progress++);
        }

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
}
