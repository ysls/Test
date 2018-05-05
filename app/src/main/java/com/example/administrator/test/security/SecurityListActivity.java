package com.example.administrator.test.security;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.test.R;
import com.example.administrator.test.base.BaseActivity;
import com.example.administrator.test.network.RetrofitServiceManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SecurityListActivity extends BaseActivity {
    @BindView(R.id.lv_list)
    ListView lvList;
    SecurityListAdapter adapter;
    List<NewsBean.ArticleListBean> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle("安全百科知识");
        setContentView(R.layout.activity_security);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SecurityListActivity.this,SecurityDetailsActivity.class);
                intent.putExtra(SecurityDetailsActivity.URL,arrayList.get(position).getUrl());
                startActivity(intent);
            }
        });

        RetrofitServiceManager.getService()
                .getArticle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NewsBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NewsBean newsBean) {
                        arrayList = newsBean.getArticleList();
                        adapter = new SecurityListAdapter(mContext,newsBean.getArticleList());
                        lvList.setAdapter(adapter);
                    }
                });
    }
}
