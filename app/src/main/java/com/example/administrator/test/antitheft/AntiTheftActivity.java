package com.example.administrator.test.antitheft;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.test.R;
import com.example.administrator.test.base.BaseActivity;
import com.example.administrator.test.util.SPUtils;

import static com.example.administrator.test.MyApplication.*;

public class AntiTheftActivity extends BaseActivity {


    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.iv_protect)
    ImageView ivProtect;
    @BindView(R.id.tv_reset)
    TextView tvReset;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, AntiTheftActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_theft);
        ButterKnife.bind(this);
        setMyTitle("手机防盗");
        initView();
    }

    @Override
    protected void initView() {
        boolean isConfig = SPUtils.getInstance().getBoolean(PREF_CONFIG);
        if (isConfig) {
            setContentView(R.layout.activity_anti_theft);
            tvPhone.setText(SPUtils.getInstance().getString(PREF_PHONE_NUMBER));
            ivProtect.setImageResource(SPUtils.getInstance().getBoolean(PREF_IS_PROTECT) ? R.mipmap.lock : R.mipmap.unlock);
            //如果没有激活权限，提示激活
            if (!mDPM.isAdminActive(mDeviceComponentName)) {
                activeAdmin();
            }
        } else {
            //没有设置过，则进入设置向导
            SetupFirstActivity.startAct(this);
            finish();
        }
    }


    @OnClick(R.id.tv_reset)
    public void onViewClicked() {
        SetupFirstActivity.startAct(AntiTheftActivity.this);
        finish();
    }
}