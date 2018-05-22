package com.example.administrator.test.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.administrator.test.MainActivity;
import com.example.administrator.test.MyApplication;
import com.example.administrator.test.R;
import com.example.administrator.test.base.BaseActivity;
import com.example.administrator.test.util.NetWorkUtil;
import com.example.administrator.test.util.RegexUtil;
import com.example.administrator.test.util.SPUtils;
import com.example.administrator.test.util.ToastUtils;



public class LoginActivity extends BaseActivity implements ILoginContract.ILoginView, View.OnClickListener {

    CountDownTimer timer;
    private static final String TAG = "LoginActivity";

    LoginPresenter loginPresenter;

    EditText etPhone;

    EditText etCode;

    TextView tvGetCode;

    Button btnLogin;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideToolbar();
        setContentView(R.layout.activity_login);
        initUI();
        loginPresenter = new LoginPresenter(this, this);
    }
    private void initUI(){
        etPhone = (EditText) findViewById(R.id.et_phone);
        etCode = (EditText) findViewById(R.id.et_code);
        tvGetCode = (TextView) findViewById(R.id.tv_get_code);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvGetCode.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }


    private void initTimer() {
        if (null == timer) {
            timer = new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvGetCode.setText(millisUntilFinished / 1000 + "s");
                }

                @Override
                public void onFinish() {
                    tvGetCode.setText("获取验证码");
                    tvGetCode.setEnabled(true);
                }
            };
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.onFinish();
        }
    }

    @Override
    public void showDialog(String msg) {
        showProgressDialog(msg);
    }

    @Override
    public void hideDialog() {
        hideProgressDialog();
    }

    @Override
    public void sendCodeSucceed() {
        timer.start();
        tvGetCode.setEnabled(false);
    }

    @Override
    public void loginSucceed(LoginBean dataBean) {
        Log.e("loginSucceed: ", dataBean.getToken());
        SPUtils.getInstance().put(MyApplication.TOKEN,dataBean.getToken());
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        Log.e("onViewClicked: ", v.getId() + "");
        if (!NetWorkUtil.isConnected()) {
            ToastUtils.showShort(R.string.Network_unavailable);
            return;
        }
        switch (v.getId()) {
            case R.id.tv_get_code:
                Log.e("onViewClicked: ", "哎哎哎");
                initTimer();
                if (!RegexUtil.isMobileExact(etPhone.getText().toString())) {
                    ToastUtils.showShort("手机格式不正确...");
                    return;
                }
                loginPresenter.getCode(etPhone.getText().toString());
                break;
            case R.id.btn_login:
                if (!RegexUtil.isMobileExact(etPhone.getText().toString()) || etCode.getText().toString().length() != 6) {
                    ToastUtils.showShort("手机格式不正确或验证码少于六位...");
                    return;
                }
                loginPresenter.login(etPhone.getText().toString(), etCode.getText().toString().trim());
                break;
            default:
        }
    }

}
