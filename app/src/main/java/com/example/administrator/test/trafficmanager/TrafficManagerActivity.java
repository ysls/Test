package com.example.administrator.test.trafficmanager;

import android.app.Activity;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.administrator.test.R;
import com.example.administrator.test.db.TrafficMessageDao;
import com.example.administrator.test.model.AppInfo;
import com.example.administrator.test.model.StorageSize;
import com.example.administrator.test.model.TrafficMessage;
import com.example.administrator.test.recever.SmsReceiver;
import com.example.administrator.test.util.*;
import com.example.administrator.test.widget.ArcProgress;

import java.text.NumberFormat;
import java.util.List;

import static com.example.administrator.test.MyApplication.PRE_TRAFFIC;


public class TrafficManagerActivity extends Activity {


    @BindView(R.id.arc_store)
    ArcProgress arcStore;
    @BindView(R.id.capacity)
    TextView capacity;
    @BindView(R.id.arc_left)
    RelativeLayout arcLeft;
    @BindView(R.id.traafic_list)
    ListView traaficList;
    @BindView(R.id.traff_correct)
    TextView traffCorrect;

    SmsReceiver receiver;

    Long total = 0L;
    long used = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);
        ButterKnife.bind(this);
        receiver = new SmsReceiver();
        String action = "android.provider.Telephony.SMS_RECEIVED";
        IntentFilter filter = new IntentFilter(action);
        String broadcastPermission = "android.permission.READ_SMS";
        registerReceiver(receiver, filter, broadcastPermission, hander);




        //全部的网络信息  wifi + 手机卡
        long totalRx = TrafficStats.getTotalRxBytes();
        long totalTx = TrafficStats.getTotalTxBytes();

        List<AppInfo> list = AppUtil.getUserAppInfos(getApplicationContext());
        initTraffic();

        traaficList.setAdapter(new TrafficAppAdapter(getApplicationContext(), list));
        onClickListener();
    }

    private void initTraffic(){
        total = SPUtils.getInstance().getLong(PRE_TRAFFIC,0);
        if (total != 0){
            used = TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes();
            NumberFormat nt = NumberFormat.getPercentInstance();
            //设置百分数精确度2即保留两位小数
            nt.setMinimumFractionDigits(0);
            float baifen = (float)(total-used)/total;
            capacity.setText(StorageUtil.convertStorage(used) + "/" + StorageUtil.convertStorage(total));
            arcStore.setProgress(baifen);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    private void onClickListener() {
        traffCorrect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsUtils.sendMsg("xcll", "10086");
                Uri smsUri = Uri.parse("content://sms");
                SmsDatabaseChaneObserver smsDatabaseChaneObserver =
                        new SmsDatabaseChaneObserver(getContentResolver(), hander, getApplicationContext());
                getContentResolver().registerContentObserver(smsUri, true, smsDatabaseChaneObserver);
            }
        });
    }

    //接收到短信的回调
    Handler hander = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SmsDatabaseChaneObserver.MESSAGE:
                    String context = (String) msg.obj;
                    TextFormater textFormater = new TextFormater();
                    List<TrafficMessage> traffentity = textFormater.formatTraffic(context);
                    TrafficMessageDao trafficMessageDao = new TrafficMessageDao(getApplicationContext());
                    trafficMessageDao.addMsg(traffentity);
                    break;
                default:
                    break;
            }
        }

    };

}
