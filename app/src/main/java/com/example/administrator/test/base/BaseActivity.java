package com.example.administrator.test.base;

import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.test.R;
import com.example.administrator.test.recever.AdminReceiver;
import com.example.administrator.test.util.InputManagerTools;


/**
 * Created by 梁遂 on 2017/8/1.
 */

public class BaseActivity extends AppCompatActivity {

    protected TextView tvTitle;
    protected Button RightButton;
    protected Toolbar toolbar;
    protected LinearLayout linear_container;
    protected LinearLayout layoutToolbar;

    protected Context mContext;

    protected ProgressDialog progressDialog;

    protected DevicePolicyManager mDPM;
    protected ComponentName mDeviceComponentName;


    protected void showProgressDialog(String msg){
        showProgressDialog(msg,false);
    }
    protected void showProgressDialog(String msg,boolean flag){
        if (null == progressDialog){
            progressDialog = new ProgressDialog(mContext);
        }
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(flag);
        progressDialog.show();
    }

    protected void hideProgressDialog(){
        if (null != progressDialog && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        mContext = this;
        initControlViews();
        // 设备策略管理器
        mDPM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        // 初始化管理员组件
        mDeviceComponentName = new ComponentName(this, AdminReceiver.class);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, linear_container);
    }

    private void initControlViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        RightButton = (Button) findViewById(R.id.cb_right_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        layoutToolbar = (LinearLayout) findViewById(R.id.layout_toolbar);
        linear_container = (LinearLayout) findViewById(R.id.linear_container);
        //设置相关默认操作
        //设置toolbar
        setSupportActionBar(toolbar);
        setTitleNavigation(R.mipmap.back);
        //取消默认的title名称
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //左边Navigation Button监听回调
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackOnClickNavigationAction(v);
            }
        });
        //右边菜单item监听回调
        RightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbackOnClickButtonAction(view);
            }
        });
    }

    /**
     * 右边按钮样式
     */
    public void setRightImageAndText(int image, String text) {
        RightButton.setVisibility(View.VISIBLE);
        RightButton.setText(text);
        if (image != 0) {
            Drawable drawable = getResources().getDrawable(image);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            RightButton.setCompoundDrawables(null, null, drawable, null);
        }
    }

    /**
     * 右边按钮点击 重写即可
     */
    protected void callbackOnClickButtonAction(View view) {}

    /**
     * 设置中间标题
     */
    public void setMyTitle(String object) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(object);
    }

    /**
     * 设置标题栏背景颜色
     */
    protected void setTitleBgColor(int color) {
        toolbar.setBackgroundColor(color);
    }

    /**
     * 初始化view
     */
    protected void initView(){

    }

    /**
     * 设置左边标题图标
     */
    public void setTitleNavigation(int iconRes) {
        if (iconRes != 0) {
            //显示左边按钮
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(iconRes);
        }
    }

    /**
     * 隐藏标题栏
     */
    public void hideToolbar() {
        if (layoutToolbar.getVisibility() == View.VISIBLE){
            layoutToolbar.setVisibility(View.GONE);
        }
    }

    /**
     * 不显示 NavigationButton
     */
    public void hideTitleNavigationButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    /**
     * Navigation Button点击回调，默认回退销毁页面
     */
    protected void callbackOnClickNavigationAction(View view) {
        onBackPressed();
    }

    /**
     *点击其他区域隐藏软键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            View v = getCurrentFocus();
            if (InputManagerTools.isShouldHideInput(v,ev)){
                if (InputManagerTools.hideInputMethod(this,v)){
                    return true;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 激活超级管理员权限 设置->安全->设备管理器     *
     */
    public void activeAdmin() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceComponentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "手机安全卫士-手机防盗，您的好帮手，值得拥有");
        startActivity(intent);
        Toast.makeText(this,"请先激活手机防盗的管理员权限",Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String s){
        Toast.makeText(this,s,0).show();
    }
}
