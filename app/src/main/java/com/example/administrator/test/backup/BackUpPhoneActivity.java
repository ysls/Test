package com.example.administrator.test.backup;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.test.R;
import com.example.administrator.test.base.BaseActivity;

public class BackUpPhoneActivity extends BaseActivity {
    @BindView(R.id.text_first)
    TextView textFirst;
    @BindView(R.id.text_second)
    TextView textSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_up_phone);
        ButterKnife.bind(this);
        setMyTitle("手机备份");
    }

    @OnClick({R.id.text_first, R.id.text_second})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_first:
                break;
            case R.id.text_second:
                break;
        }
    }
}
