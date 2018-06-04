package com.example.administrator.test.antitheft;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import com.example.administrator.test.R;
import com.example.administrator.test.base.BaseActivity;
import com.example.administrator.test.login.CodeBean;
import com.example.administrator.test.network.RetrofitServiceManager;
import com.example.administrator.test.util.NetWorkUtil;
import com.example.administrator.test.util.SPUtils;

import static com.example.administrator.test.MyApplication.PREF_PHONE_NUMBER;
import static com.example.administrator.test.MyApplication.TOKEN;


public class SetupThirdActivity extends BaseActivity {
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.btn_select_contact)
    Button btnSelectContact;
    @BindView(R.id.btn_next_page)
    Button btnNextPage;
    @BindView(R.id.btn_previous_page)
    Button btnPreviousPage;
    String mPhoneNumber;
    public static void startAct(Context context) {
        Intent intent = new Intent(context, SetupThirdActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_third);
        ButterKnife.bind(this);
        setMyTitle("3.设置安全号码");
        mPhoneNumber = SPUtils.getInstance().getString(PREF_PHONE_NUMBER);
        etNumber.setText(mPhoneNumber);
    }
    protected void previousPage() {
        SetupSecondActivity.startAct(this);
        finish();
        // activity切换动画
        overridePendingTransition(R.anim.anim_previous_in,
                R.anim.anim_previous_out);
    }
    protected void nextPage() {
        mPhoneNumber = etNumber.getText().toString();
        if (!TextUtils.isEmpty(mPhoneNumber)) {

            if (!NetWorkUtil.isConnected()){
                Toast.makeText(this,"无网络连接...",0).show();
                return;
            }
            if(mPhoneNumber.startsWith("+86")) mPhoneNumber = mPhoneNumber.substring(3);

            //保存安全号码
            SPUtils.getInstance().put(PREF_PHONE_NUMBER,mPhoneNumber);
            RetrofitServiceManager.getService().setSafeNum(SPUtils.getInstance().getString(TOKEN),mPhoneNumber)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(new Subscriber<CodeBean>() {
                        @Override
                        public void onCompleted() { }
                        @Override
                        public void onError(Throwable e) { }
                        @Override
                        public void onNext(CodeBean codeBean) {
                                if (codeBean.getFlag() == 1){
                                    SetupFourActivity.startAct(mContext);
                                    finish();
                                    // activity切换动画
                                    overridePendingTransition(R.anim.anim_next_in,
                                            R.anim.anim_next_out);
                                }else {
                                    Toast.makeText(SetupThirdActivity.this,"安全手机号上传失败",0).show();
                                }
                        }
                    });
        } else {
            Toast.makeText(this,"安全手机号不能为空",0).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            mPhoneNumber = data.getStringExtra("phone");
            mPhoneNumber = mPhoneNumber.replaceAll("-", "").replaceAll(" ", "");
            etNumber.setText(mPhoneNumber);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @OnClick({R.id.btn_select_contact, R.id.btn_next_page, R.id.btn_previous_page})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_select_contact:
                SelectContactActivity.startActForResult(this);
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