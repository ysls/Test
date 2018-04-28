package com.example.administrator.test.antitheft;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.test.R;
import com.example.administrator.test.base.BaseActivity;
import com.example.administrator.test.util.SPUtils;
import com.example.administrator.test.widget.SettingItemView;

import static com.example.administrator.test.MyApplication.PREF_BIND_SIM;


public class SetupSecondActivity extends BaseActivity {


    @BindView(R.id.imageView1)
    ImageView imageView1;
    @BindView(R.id.btn_next_page)
    Button btnNextPage;
    @BindView(R.id.btn_previous_page)
    Button btnPreviousPage;
    @BindView(R.id.siv_bind_sim)
    SettingItemView sivBindSim;

    private String mSimSerialNumber;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, SetupSecondActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_second);
        ButterKnife.bind(this);
        setMyTitle("手机卡绑定");
        mSimSerialNumber = SPUtils.getInstance().getString(PREF_BIND_SIM);
        sivBindSim.setChecked(!TextUtils.isEmpty(mSimSerialNumber));
    }


    private void binSIM(){
        sivBindSim.setChecked(!sivBindSim.isChecked());
        if (sivBindSim.isChecked()) {
            //获取手机sim卡序列号
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            mSimSerialNumber = tm.getSimSerialNumber();
            SPUtils.getInstance().put(PREF_BIND_SIM,mSimSerialNumber);
        } else {
            SPUtils.getInstance().remove(PREF_BIND_SIM);
            mSimSerialNumber = "";
        }
    }


    protected void previousPage() {
        SetupFirstActivity.startAct(this);
        finish();
        // activity切换动画
        overridePendingTransition(R.anim.anim_previous_in,
                R.anim.anim_previous_out);
    }

    protected void nextPage() {
        if (TextUtils.isEmpty(mSimSerialNumber)) {
            showToast("必须绑定sim卡");
        } else {

            SetupThirdActivity.startAct(this);
            finish();
            // activity切换动画
            overridePendingTransition(R.anim.anim_next_in,
                    R.anim.anim_next_out);
        }
    }

    @OnClick({R.id.siv_bind_sim, R.id.btn_next_page, R.id.btn_previous_page})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.siv_bind_sim:
                binSIM();
                break;
            case R.id.btn_next_page:
                nextPage();
                break;
            case R.id.btn_previous_page:
                previousPage();
                break;
        }
    }
}