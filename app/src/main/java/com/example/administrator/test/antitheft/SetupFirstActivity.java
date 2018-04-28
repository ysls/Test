package com.example.administrator.test.antitheft;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.test.R;
import com.example.administrator.test.base.BaseActivity;


public class SetupFirstActivity extends BaseActivity {

    @BindView(R.id.imageView1)
    ImageView imageView1;
    @BindView(R.id.btn_next_page)
    Button btnNextPage;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, SetupFirstActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_first);
        ButterKnife.bind(this);
        setMyTitle("1.欢迎使用手机防盗");
    }


    protected void nextPage() {
        SetupSecondActivity.startAct(this);
        finish();
        // activity切换动画
        overridePendingTransition(R.anim.anim_next_in,
                R.anim.anim_next_out);
    }

    @OnClick(R.id.btn_next_page)
    public void onViewClicked() {
        nextPage();
    }
}