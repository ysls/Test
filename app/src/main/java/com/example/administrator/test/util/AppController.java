package com.example.administrator.test.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class AppController {
	/**
     * ����: ��װ
     */
    public static boolean install(String apkPath, Context context){
        // ���ж��ֻ��Ƿ���rootȨ��
        if(hasRootPerssion()){
            // ��rootȨ�ޣ����þ�Ĭ��װʵ��
            return clientInstall(apkPath);
        }else{
            // û��rootȨ�ޣ�������ͼ���а�װ
            File file = new File(apkPath);
            if(!file.exists())
                return false; 
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
            context.startActivity(intent);
            return true;
        }
    }
     
    /**
     * ����: ж��
     *                                        
     * 
     */
    public static boolean uninstall(String packageName, Context context){
        if(hasRootPerssion()){
            // ��rootȨ�ޣ����þ�Ĭж��ʵ��
        	System.out.println("--------------------��rootȨ��");
            return clientUninstall(packageName);
        }else{
        	System.out.println("--------------------û��rootȨ��");
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,packageURI);
            uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(uninstallIntent);
            return true;
        }
    }
     
    /**
     * �ж��ֻ��Ƿ���rootȨ��
     */
    private static boolean hasRootPerssion(){
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();  
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(process!=null){
                process.destroy();
            }
        }
        return false;
    }
     
    /**
     * ��Ĭ��װ
     */
    private static boolean clientInstall(String apkPath){
        PrintWriter PrintWriter = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("chmod 777 "+apkPath);
            PrintWriter.println("export LD_LIBRARY_PATH=/vendor/lib:/system/lib");
            PrintWriter.println("pm install -r "+apkPath);
//          PrintWriter.println("exit");
            PrintWriter.flush();
            PrintWriter.close();
            int value = process.waitFor();  
            return returnResult(value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(process!=null){
                process.destroy();
            }
        }
        return false;
    }
     
    /**
     * ��Ĭж��
     */
    public static boolean clientUninstall(String packageName){
        PrintWriter PrintWriter = null;
        Process process = null;
      //  try {
            try {
				process = Runtime.getRuntime().exec("su");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            PrintWriter = new PrintWriter(process.getOutputStream());
            PrintWriter.println("LD_LIBRARY_PATH=/vendor/lib:/system/lib ");
            PrintWriter.println("pm uninstall "+packageName);
            PrintWriter.flush();
            PrintWriter.close();
            int value = 0;
			try {
				value = process.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
            return returnResult(value); 
        //} catch (Exception e) {
          //  e.printStackTrace();
        //}
   // finally{
            //if(process!=null){
            //    process.destroy();
          //  }
       // }
        //return false;
    }
     
    /**
     * ����app
     * com.exmaple.client/.MainActivity
     * com.exmaple.client/com.exmaple.client.MainActivity
     */
    public static boolean startApp(String packageName, String activityName){
        boolean isSuccess = false;
     //  packageName + "/" + activityName + " \n";
       String cmd = "am start -n " + new ComponentName(packageName,activityName).flattenToShortString();
        System.out.println(cmd);
        Process process = null;
        try {
           process = Runtime.getRuntime().exec(cmd);
           int value = process.waitFor();  
           System.out.println(value);
           
           return returnResult(value);
        } catch (Exception e) {
          e.printStackTrace();
        } finally{
            if(process!=null){
                process.destroy();
            }
        }
        return isSuccess;
    }
     
     
    private static boolean returnResult(int value){
        // �����ɹ�  
        if (value == 0) {
            return true;
        } else if (value == 1) { // ʧ��
            return false;
        } else { // δ֪���
            return false;
        }  
    }
}