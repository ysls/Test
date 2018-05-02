package com.example.administrator.test.security;

import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.administrator.test.R;
import com.example.administrator.test.base.BaseActivity;

public class SecurityDetailsActivity extends BaseActivity {
    @BindView(R.id.webView)
    WebView webView;

    public static String URL = "url";
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_details);
        setMyTitle("内容详情");
        ButterKnife.bind(this);
        url = getIntent().getStringExtra(URL);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });

        webView.loadUrl(url);
    }
}
