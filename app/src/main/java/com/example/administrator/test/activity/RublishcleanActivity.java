package com.example.administrator.test.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.*;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.administrator.test.R;
import com.example.administrator.test.adapter.RublishMemoryAdapter;
import com.example.administrator.test.model.AppProcessInfo;
import com.example.administrator.test.model.CacheListItem;
import com.example.administrator.test.model.StorageSize;
import com.example.administrator.test.service.CleanerService;
import com.example.administrator.test.util.AppUtil;
import com.example.administrator.test.util.StorageUtil;
import com.example.administrator.test.widget.ArcProgress;
import com.example.administrator.test.widget.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;


/**
 * 扫描垃圾
 *
 * @author 文江
 */
public class RublishcleanActivity extends Activity implements CleanerService.OnActionListener {
    protected static final int SCANING = 5;

    protected static final int SCAN_FINIFSH = 6;
    protected static final int PROCESS_MAX = 8;
    protected static final int PROCESS_PROCESS = 9;
    private static final int READMORM = 0x0;
    protected Context mContext;
    private static final int INITIAL_DELAY_MILLIS = 300;
    Resources res;
    int ptotal = 0;
    int pprocess = 0;

    @BindView(R.id.rubbish_capacity)
    TextView rubbishCapacity;
    @BindView(R.id.arc_left)
    RelativeLayout arcLeft;
    @BindView(R.id.rubbish_list)
    ListView rubbishList;
    @BindView(R.id.rubbish_oneKeydo)
    Button rubbishOneKeydo;

    @BindView(R.id.progressBarText)
    TextView progressBarText;
    @BindView(R.id.progressBar)
    LinearLayout progressBar;
    @BindView(R.id.rubbish_arc_store)
    ArcProgress rubbishArcStore;
    @BindView(R.id.progressBar2)
    CircularProgressBar progressBar2;


    private CleanerService mCleanerService;

    private boolean mAlreadyScanned = false;
    private boolean mAlreadyCleaned = false;


    List<CacheListItem> mCacheListItem = new ArrayList<CacheListItem>();

    RublishMemoryAdapter rublishMemoryAdapter;


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mCleanerService = ((CleanerService.CleanerServiceBinder) service).getService();
            mCleanerService.setOnActionListener(RublishcleanActivity.this);
            if (!mCleanerService.isScanning() && !mAlreadyScanned) {
                mCleanerService.scanCache();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCleanerService.setOnActionListener(null);
            mCleanerService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubbish);
        ButterKnife.bind(this);

        mContext = this;
        List<AppProcessInfo> list = AppUtil.getRunningAppProcesses(getApplicationContext());
        Thread thread = new Thread(new Runnable() {
            //获取运行内存大小
            @Override
            public void run() {
                long alMemory = AppUtil.getTotalMemory(mContext);
                long availMemory = AppUtil.getAvailMemory(mContext);
                StorageSize alMemorySize = StorageUtil.convertStorageSize(alMemory);
                StorageSize availMemorySize = StorageUtil.convertStorageSize(availMemory);
                Message msMessage = Message.obtain();
                msMessage.what = READMORM;
                String value = (alMemorySize.value + "").substring(0, 4);
                String valueall = (availMemorySize.value + "").substring(0, 4);
                msMessage.obj = valueall + alMemorySize.suffix + "/"
                        + value + availMemorySize.suffix;

                handler.sendMessage(msMessage);
            }
        });
        thread.start();

        applyKitKatTranslucency();
        res = getResources();
        rublishMemoryAdapter = new RublishMemoryAdapter(mContext, mCacheListItem);
        rubbishList.setAdapter(rublishMemoryAdapter);
        rubbishList.setOnItemClickListener(rublishMemoryAdapter);
        bindService(new Intent(mContext, CleanerService.class),
                mServiceConnection, Context.BIND_AUTO_CREATE);
        final Button rubbish_oneKeydo = (Button) findViewById(R.id.rubbish_oneKeydo);//
        rubbish_oneKeydo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rubbish_oneKeydo.getText().equals("完成")) {
                    finish();
                } else {
                    onClickClear();
                    rubbish_oneKeydo.setText("完成");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScanStarted(Context context) {
        progressBarText.setText(R.string.scanning);
        showProgressBar(true);
    }

    @Override
    public void onScanProgressUpdated(Context context, int current, int max) {
        progressBarText.setText(getString(R.string.scanning_m_of_n, current, max));

    }


    @Override
    public void onScanCompleted(Context context, List<CacheListItem> apps) {
        showProgressBar(false);
        mCacheListItem.clear();
        mCacheListItem.addAll(apps);
        rublishMemoryAdapter.notifyDataSetChanged();
        long medMemory = mCleanerService != null ? mCleanerService.getCacheSize() : 0;
        System.out.println(medMemory);
        float cachsize = 0;
        for (CacheListItem cacheListItem : apps) {
            cachsize += cachsize + cacheListItem.getCacheSize();
        }
        StorageSize mStorageSize = StorageUtil.convertStorageSize(medMemory);
        System.out.println("mStorageSize.value" + mStorageSize.value);
        if (mStorageSize.value == 0.0) {
            rubbishArcStore.setSuffixText("");
            rubbishArcStore.setBottomText("暂无垃圾");
            rubbishCapacity.setText("");
        } else {
            rubbishArcStore.setProgress(mStorageSize.value);
            rubbishArcStore.setSuffixText(mStorageSize.suffix);
        }

    }

    @Override
    public void onCleanStarted(Context context) {
        if (isProgressBarVisible()) {
            showProgressBar(false);
        }
        if (!RublishcleanActivity.this.isFinishing()) {
            //showDialogLoading();
        }
    }

    @Override
    public void onCleanCompleted(Context context, long cacheSize) {
        //  dismissDialogLoading();
//        Toast.makeText(context, context.getString(R.string.cleaned, Formatter.formatShortFileSize(
//                mContext, cacheSize)), Toast.LENGTH_LONG).show();
        mCacheListItem.clear();
        rublishMemoryAdapter.notifyDataSetChanged();
    }


    private void applyKitKatTranslucency() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            setTranslucentStatus(true);
        }

    }


    public void onClickClear() {
        if (mCleanerService != null && !mCleanerService.isScanning() &&
                !mCleanerService.isCleaning() && mCleanerService.getCacheSize() > 0) {
            mAlreadyCleaned = false;
            mCleanerService.cleanCache();
        }
        rubbishArcStore.setProgress(0);
        rubbishArcStore.setSuffixText("");
        rubbishArcStore.setBottomText("已清理");
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    private boolean isProgressBarVisible() {
        return progressBar.getVisibility() == View.VISIBLE;
    }

    private void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.startAnimation(AnimationUtils.loadAnimation(
                    mContext, android.R.anim.fade_out));
            progressBar.setVisibility(View.GONE);
        }
    }

    public void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case READMORM:
                    String message = (String) msg.obj;
                    rubbishCapacity.setText(message);
                    break;

                default:
                    break;
            }
        }

        ;
    };

}
