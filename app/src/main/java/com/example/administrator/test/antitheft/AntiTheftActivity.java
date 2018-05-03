package com.example.administrator.test.antitheft;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.test.R;
import com.example.administrator.test.base.BaseActivity;
import com.example.administrator.test.util.SPUtils;

import static com.example.administrator.test.MyApplication.*;

public class AntiTheftActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {


    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_reset)
    TextView tvReset;
    @BindView(R.id.text_gps)
    TextView textGps;
    @BindView(R.id.text_music)
    TextView textMusic;
    @BindView(R.id.text_wipe)
    TextView textWipe;
    @BindView(R.id.text_lock)
    TextView textLock;
    @BindView(R.id.sw_protect)
    Switch swProtect;
    @BindView(R.id.text_photo)
    TextView textPhoto;
    @BindView(R.id.text_voice)
    TextView textVoice;

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
            swProtect.setChecked(SPUtils.getInstance().getBoolean(PREF_IS_PROTECT));
            swProtect.setOnCheckedChangeListener(this);
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

    @Override
    protected void onResume() {
        super.onResume();
        textGps.setText("GPS追踪:" + SPUtils.getInstance().getString(SECRET_GPS,"#GPSLOCATION#"));
        textMusic.setText("播放报警音乐:" + SPUtils.getInstance().getString(SECRET_ALARM,"#ALARMMUSICE#"));
        textWipe.setText("远程删除数据:" + SPUtils.getInstance().getString(SECRET_WIPE,"#WIPEDATA#"));
        textLock.setText("远程锁屏:" + SPUtils.getInstance().getString(SECRET_LOCK,"LOCKSCREEEN"));
        textPhoto.setText("远程拍照:" + SPUtils.getInstance().getString(SECRET_PHOTO,"TAKEPHOTO"));
        textVoice.setText("远程录音:" + SPUtils.getInstance().getString(SECRET_VOICE,"RECORD"));
    }

    @OnClick(R.id.tv_reset)
    public void onViewClicked() {
        SetupFirstActivity.startAct(AntiTheftActivity.this);
        finish();
    }

    @OnClick({R.id.text_gps, R.id.text_music, R.id.text_wipe, R.id.text_lock,R.id.text_voice,R.id.text_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_gps:
                showWordDialog(SPUtils.getInstance().getString(SECRET_GPS), SECRET_GPS);
                break;
            case R.id.text_music:
                showWordDialog(SPUtils.getInstance().getString(SECRET_ALARM), SECRET_ALARM);
                break;
            case R.id.text_wipe:
                showWordDialog(SPUtils.getInstance().getString(SECRET_WIPE), SECRET_WIPE);
                break;
            case R.id.text_lock:
                showWordDialog(SPUtils.getInstance().getString(SECRET_LOCK), SECRET_LOCK);
                break;
            case R.id.text_voice:
                showWordDialog(SPUtils.getInstance().getString(SECRET_VOICE), SECRET_VOICE);
                break;
            case R.id.text_photo:
                showWordDialog(SPUtils.getInstance().getString(SECRET_PHOTO), SECRET_PHOTO);
                break;
        }
    }

    private void showWordDialog(final String text, final String code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_set_word, null);
        final EditText et_set_word = (EditText) view.findViewById(R.id.et_set_word);
        et_set_word.setText(text);
        dialog.setView(view);
        dialog.show();

        view.findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String word = et_set_word.getText().toString().trim();
                if (!TextUtils.isEmpty(word)) {
                    SPUtils.getInstance().put(code, word);
                    Log.e("log", "密语为" + word + "设置成功");
                    dialog.dismiss();
                    onResume();
                } else {
                    showToast("密语不能为空。。。");
                }
            }
        });

        view.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SPUtils.getInstance().put(PREF_IS_PROTECT,isChecked);
    }
}