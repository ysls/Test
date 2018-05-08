package com.example.administrator.test;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.administrator.test.activity.RublishcleanActivity;
import com.example.administrator.test.adapter.HomeAdapter;
import com.example.administrator.test.antitheft.AntiTheftActivity;
import com.example.administrator.test.backup.BackUpPhoneActivity;
import com.example.administrator.test.base.BaseActivity;
import com.example.administrator.test.blacknumber.BlackNumberActivity;
import com.example.administrator.test.model.PhoneCodeBean;
import com.example.administrator.test.network.RetrofitServiceManager;
import com.example.administrator.test.security.SecurityListActivity;
import com.example.administrator.test.service.LocationService;
import com.example.administrator.test.takephoto.CameraActivity;
import com.example.administrator.test.trafficmanager.TrafficManagerActivity;
import com.example.administrator.test.util.Md5Utils;
import com.example.administrator.test.util.SPUtils;
import com.example.administrator.test.virus.AntiVirusActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.example.administrator.test.MyApplication.PREF_PASSWORD;
import static com.example.administrator.test.MyApplication.PREF_PHONE_NUMBER;
import static com.example.administrator.test.MyApplication.TOKEN;

public class MainActivity extends BaseActivity {


    @BindView(R.id.gv_fuction)
    GridView gvFuction;

    private int[] mImgIds;
    private String[] mGvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setMyTitle("PuppyPhone");
        hideTitleNavigationButton();
        initView();
        initData();
    }

    private void initData() {
        Log.i("TOKEN", SPUtils.getInstance().getString(TOKEN));
        RetrofitServiceManager.getService()
                .getSafeNum(SPUtils.getInstance().getString(TOKEN))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<PhoneCodeBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(PhoneCodeBean phoneCodeBean) {
                        SPUtils.getInstance().put(PREF_PHONE_NUMBER,phoneCodeBean.getSafe_num());
                    }
                });
    }

    @Override
    protected void initView() {
        super.initView();
        mGvItems = new String[]{"手机防盗", "安全百科", "黑名单", "垃圾清理", "病毒查杀", "流量统计", "手机备份", "软件说明", "退出登录"};
        mImgIds = new int[]{R.mipmap.ic_lock,
                R.mipmap.ic_book, R.mipmap.ic_black,
                R.mipmap.ic_clean, R.mipmap.ic_virus,
                R.mipmap.ic_total, R.mipmap.ic_backup,
                R.mipmap.ic_introduce, R.mipmap.ic_out};
        gvFuction.setAdapter(new HomeAdapter(this,mGvItems,mImgIds));

        gvFuction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0://手机防盗
                            showSafeDialog();
                            break;
                        case 1://安全百科
                            startActivity(SecurityListActivity.class);
                            break;
                        case 2://黑名单
                            startActivity(BlackNumberActivity.class);
                            break;
                        case 3://垃圾清理
                             startActivity(RublishcleanActivity.class);
                            break;
                        case 4://病毒查杀
                            startActivity(AntiVirusActivity.class);
                            break;
                        case 5://流量统计
                            startActivity(TrafficManagerActivity.class);
                            break;
                        case 6://手机备份
                            startActivity(BackUpPhoneActivity.class);
                            break;
                        case 7://软件说明
                            startActivity(CameraActivity.class);
                            break;
                        case 8://退出登录
                            Intent intent = new Intent(mContext,LocationService.class);
                        startService(intent);
                            break;
                            default:
                    }
            }
        });
    }

    /**
     * 显示手机防盗弹窗
     */
    private void showSafeDialog() {
        String password = SPUtils.getInstance().getString(PREF_PASSWORD);

        //判断是否存在密码
        if (!TextUtils.isEmpty(password)) {
            //显示输入密码弹窗
            showInputPasswordDialog();
        } else {
            //显示设置密码弹窗
            showSetPasswordDialog();
        }

    }

    /**
     * 输入密码的弹窗
     */
    private void showInputPasswordDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.dialog_input_password, null);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        final EditText etPassword = (EditText) view.findViewById(R.id.et_password);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString().trim();
                //获取保存的密码
                String savedPassword = SPUtils.getInstance().getString(PREF_PASSWORD);

                //判断密码是否为空
                if (!TextUtils.isEmpty(password)) {
                    if (Md5Utils.encode(password).equals(savedPassword)) {
                        startActivity(AntiTheftActivity.class);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }

    /**
     * 设置密码的弹窗
     */
    private void showSetPasswordDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.dialog_set_password, null);
        Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
        final EditText etPasswordConfirm = (EditText) view.findViewById(R.id.et_password_confirm);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString().trim();
                String passwordConfirm = etPasswordConfirm.getText().toString().trim();

                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordConfirm)) {
                    if (password.equals(passwordConfirm)) {
                        SPUtils.getInstance().put(PREF_PASSWORD, Md5Utils.encode(password));
                        startActivity(AntiTheftActivity.class);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this,"密码不一致",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }

    private void startActivity(Class cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
    }
}
