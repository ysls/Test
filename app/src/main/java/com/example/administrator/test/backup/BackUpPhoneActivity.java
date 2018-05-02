package com.example.administrator.test.backup;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.test.R;
import com.example.administrator.test.base.BaseActivity;
import com.example.administrator.test.util.BackUpUtils;

public class BackUpPhoneActivity extends BaseActivity implements BackUpUtils.BackUpCallback {
    @BindView(R.id.text_first)
    TextView textFirst;
    @BindView(R.id.text_second)
    TextView textSecond;

    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_up_phone);
        ButterKnife.bind(this);
        setMyTitle("手机备份");
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
    }

    @OnClick({R.id.text_first, R.id.text_second})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_first:
                dialog.setTitle("正在备份号码");
                dialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BackUpUtils.upload(BackUpPhoneActivity.this,BackUpPhoneActivity.this);
                    }
                }).start();
                break;
            case R.id.text_second:
                dialog.setTitle("正在还原号码");
                dialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BackUpUtils.download(BackUpPhoneActivity.this,BackUpPhoneActivity.this);
                    }
                }).start();
                
                break;
        }
    }

    @Override
    public void preSmsBackup(int totalCount) {
        dialog.setMax(totalCount);
    }

    @Override
    public void onSmsBackup(int progress) {
        dialog.setProgress(progress);
    }
}
