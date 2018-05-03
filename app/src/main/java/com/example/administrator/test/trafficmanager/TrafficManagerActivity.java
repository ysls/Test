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
import com.example.administrator.test.util.AppUtil;
import com.example.administrator.test.util.SmsUtils;
import com.example.administrator.test.util.StorageUtil;
import com.example.administrator.test.util.TextFormater;
import com.example.administrator.test.widget.ArcProgress;

import java.util.List;


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
        //traffice_round.setDefaultStr(50+"/100M");
        //		traffice_round.set
        //rx receive 接收 下载
        //手机的2g/3g/4g 产生流量
        long mobileRx = TrafficStats.getMobileRxBytes();//接收
        //transfer 发送  上传
        StorageSize storageSize = StorageUtil.convertStorageSize(mobileRx);
        long mobileTx = TrafficStats.getMobileTxBytes();
        StorageSize storageSize1 = StorageUtil.convertStorageSize(mobileTx);
        System.out.println(storageSize1.value + storageSize1.suffix);

        //全部的网络信息  wifi + 手机卡
        long totalRx = TrafficStats.getTotalRxBytes();
        long totalTx = TrafficStats.getTotalTxBytes();
        SmsManager manager = SmsManager.getDefault();
        //
        //uid 用户id
        int uid = 0;

        List<AppInfo> list = AppUtil.getUserAppInfos(getApplicationContext());

        ///proc/uid_stat/10041/tcp_rcv存储的就是下载的流量
        //proc/uid_stat/10041/tcp_snd 上传的流量
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netinfor = cm.getActiveNetworkInfo();
        TrafficMessageDao trafficMessageDao = new TrafficMessageDao(this);
        /*	if(netinfor!=null){
            System.out.println(netinfor.getTypeName());
		}*/
        List<TrafficMessage> all = trafficMessageDao.getTrafficMessageAll();
        double alltraffic = 0;
        double used = 0;
        for (TrafficMessage trafficMessage : all) {
            used = used + Double.parseDouble(trafficMessage.getApplyed());
            alltraffic += Double.parseDouble(trafficMessage.getAll());
        }
        capacity.setText(used + "/" + alltraffic + "M");
        arcStore.setSuffixText("M");
        arcStore.setProgress((float) 50);

        traaficList.setAdapter(new TrafficAppAdapter(getApplicationContext(), list));
        onClickListener();
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
                SmsUtils.sendMsg("5011", "10086");
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
