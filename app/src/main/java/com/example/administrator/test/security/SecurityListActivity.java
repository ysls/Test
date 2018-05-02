package com.example.administrator.test.security;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.administrator.test.R;
import com.example.administrator.test.base.BaseActivity;

import java.util.ArrayList;

public class SecurityListActivity extends BaseActivity {
    @BindView(R.id.lv_list)
    ListView lvList;
    SecurityListAdapter adapter;
    ArrayList<NewsBean.ArticleListBean> arrayList;

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
        arrayList = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            NewsBean.ArticleListBean bean = new NewsBean.ArticleListBean();
            bean.setTitle("看完这篇文章 你还敢不重视智能手机的安全么?"+i);
            bean.setUrl("http://101.132.151.168:8080/PhoneSafe/showArticle?tid=20044");
            arrayList.add(bean);
        }

        adapter = new SecurityListAdapter(this,arrayList);
        lvList.setAdapter(adapter);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SecurityListActivity.this,SecurityDetailsActivity.class);
                intent.putExtra(SecurityDetailsActivity.URL,arrayList.get(position).getUrl());
                startActivity(intent);
            }
        });
    }
}
