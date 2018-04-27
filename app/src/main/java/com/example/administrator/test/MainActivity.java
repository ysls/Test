package com.example.administrator.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.test.activity.RublishcleanActivity;
import com.example.administrator.test.trafficmanager.TrafficManagerActivity;
import com.example.administrator.test.virus.AntiVirusActivity;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.tv_clean)
    TextView tvClean;
    @BindView(R.id.tv_liu)
    TextView tvLiu;
    @BindView(R.id.tv_kill)
    TextView tvKill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.tv_clean, R.id.tv_liu, R.id.tv_kill})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_clean:
                startActivity(new Intent(this, RublishcleanActivity.class));
                break;
            case R.id.tv_liu:
                startActivity(new Intent(this, TrafficManagerActivity.class));
                break;
            case R.id.tv_kill:
                startActivity(new Intent(this, AntiVirusActivity.class));
                break;
        }
    }
}
