package com.example.administrator.test.util;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class InputManagerTools {

    //判断点击事件是否在EditText上
    public static boolean isShouldHideInput(View v, MotionEvent event){
        if (v != null && v instanceof EditText){
            int[] leftTop = {0,0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], right = left + v.getWidth(), bottom = top + v.getHeight();
            float x = event.getX(),y = event.getY();
            return !(x > left && x < right && y > top && y < bottom);
        }
        return false;
    }

    public static boolean hideInputMethod(Context context,View v){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null){
            return imm.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
        return false;
    }
}
