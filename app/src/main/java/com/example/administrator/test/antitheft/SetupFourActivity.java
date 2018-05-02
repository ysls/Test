package com.example.administrator.test.antitheft;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.test.R;
import com.example.administrator.test.base.BaseActivity;
import com.example.administrator.test.util.SPUtils;

import static com.example.administrator.test.MyApplication.PREF_CONFIG;
import static com.example.administrator.test.MyApplication.PREF_IS_PROTECT;


public class SetupFourActivity extends BaseActivity {

    @BindView(R.id.cb_check)
    CheckBox cbCheck;
    @BindView(R.id.btn_next_page)
    Button btnNextPage;
    @BindView(R.id.btn_previous_page)
    Button btnPreviousPage;
    private boolean mIsProtect;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, SetupFourActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_four);
        ButterKnife.bind(this);
        setMyTitle("4.恭喜您,设置完成");
        initView();
    }


    @Override
    protected void initView() {

        mIsProtect = SPUtils.getInstance().getBoolean(PREF_IS_PROTECT);
        cbCheck.setChecked(mIsProtect);
        cbCheck.setText(mIsProtect ? "您已开启防盗保护" : "您没有开启防盗保护");



        cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsProtect = isChecked;
                SPUtils.getInstance().put(PREF_IS_PROTECT,isChecked);
                cbCheck.setText(isChecked ? "您已开启防盗保护" : "您没有开启防盗保护");

                if (isChecked) {
                    if (!mDPM.isAdminActive(mDeviceComponentName)) {
                        activeAdmin();
                    }
                } else {
                    mDPM.removeActiveAdmin(mDeviceComponentName);
                }
            }
        });
    }



    protected void previousPage() {
        SetupThirdActivity.startAct(this);
        finish();
        // activity切换动画
        overridePendingTransition(R.anim.anim_previous_in,
                R.anim.anim_previous_out);
    }

    protected void nextPage() {
        SPUtils.getInstance().put(PREF_CONFIG,true);
        AntiTheftActivity.startAct(this);
        finish();
        // activity切换动画
        overridePendingTransition(R.anim.anim_next_in,
                R.anim.anim_next_out);
    }

    @OnClick({R.id.btn_next_page, R.id.btn_previous_page})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next_page:
                nextPage();
                break;
            case R.id.btn_previous_page:
                previousPage();
                break;
        }
    }
}