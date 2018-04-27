package com.example.administrator.test.trafficmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.TrafficStats;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.administrator.test.R;
import com.example.administrator.test.model.StorageSize;
import com.example.administrator.test.util.StorageUtil;
import com.example.administrator.test.model.AppInfo;


import java.util.ArrayList;
import java.util.List;

public class TrafficAppAdapter extends BaseAdapter {
	private LayoutInflater layoutflater;
	private List<AppInfo> appinfos;
	private Context context;
	public TrafficAppAdapter(Context context, List<AppInfo> appinfos) {
		this.layoutflater= LayoutInflater.from(context);
		this.appinfos=appinfos;
		this.context=context;
	}
	
	@Override
	public int getCount() {
		return appinfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return appinfos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		TrafficStats.getUidRxBytes(10041);
		TrafficStats.getUidTxBytes(10041);
		View view;
		ViewHolder holder;
			view = layoutflater.inflate(R.layout.adapter_traffic_item,null);
			holder=new ViewHolder();
			holder.app_name=(TextView) view.findViewById(R.id.app_name);
			holder.app_name.setText(appinfos.get(position).getName());
			holder.app_image=(ImageView) view.findViewById(R.id.app_image);
			holder.app_image.setBackgroundDrawable(appinfos.get(position).getIcon());
			holder.spinner=(Spinner) view.findViewById(R.id.spinner1);
//			holder.spinner.setAdapter(new SpinnerAdapter(context));
			holder.gprs=(TextView) view.findViewById(R.id.gprs);
			System.out.println("uid="+appinfos.get(position).getUid());
			long l= TrafficStats.getUidRxBytes(appinfos.get(position).getUid())+ TrafficStats.getUidTxBytes(appinfos.get(position).getUid());
			StorageSize size= StorageUtil.convertStorageSize(l);
			double d=((int)(size.value*100))/100;  
			holder.gprs.setText(d+size.suffix);
			view.setTag(holder);
		return view;
	}
	
	static class ViewHolder{
		ImageView app_image;
		TextView app_name,gprs;
		Spinner spinner;
		
	}
	
//	List<String> list=new ArrayList<String>();
//	class SpinnerAdapter extends BaseAdapter {
//		Context context;
//		public SpinnerAdapter(Context context) {
//			list.clear();
//			list.add("����");
//			list.add("��ֹ");
//			list.add("����̨");
//             this.context=context;
//		}
//
//		@Override
//		public int getCount() {
//			return list.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return list.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return 0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			      View view=null;
//			      if(view==null){
//			    	  view= LayoutInflater.from(context).inflate(R.layout.popup_item, null);
//			           TextView action= (TextView) view.findViewById(R.id.action);
//			             action.setText(list.get(position));
//			      }
//			return view;
//		}
//
//	}
}
