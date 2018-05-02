package com.example.administrator.test.security;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.administrator.test.R;

import java.util.ArrayList;

public class SecurityListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<NewsBean.ArticleListBean> mList;
    public SecurityListAdapter(Context context, ArrayList<NewsBean.ArticleListBean> listBeans){
        mContext = context;
        mList = listBeans;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.item_act_select_contact,null);
            viewHolder = new ViewHolder();
            viewHolder.textTitle = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.textUrl = (TextView) convertView.findViewById(R.id.tv_phone);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textTitle.setText(mList.get(position).getTitle());
        viewHolder.textUrl.setText(mList.get(position).getUrl());
        return convertView;
    }

    public class ViewHolder{
         TextView textTitle;
         TextView textUrl;
    }

}
