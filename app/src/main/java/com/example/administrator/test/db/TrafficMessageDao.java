package com.example.administrator.test.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.administrator.test.model.TrafficMessage;

import java.util.ArrayList;
import java.util.List;

public class TrafficMessageDao {
	public TrafficMessageDBOOpenHelper helper;
	SQLiteDatabase db;
	public TrafficMessageDao(Context context){
		helper=new TrafficMessageDBOOpenHelper(context);
		db = helper.getWritableDatabase();
	}


	public void addMsg(TrafficMessage entity){

		ContentValues values = new ContentValues();
		values.put("typeContext", entity.getTypeContext());
		values.put("applyed", entity.getApplyed());
		values.put("surplus", entity.getSurplus());
		values.put("alled", entity.getAll());
		Cursor cursor = db.query("trafficMessage",
				new String[]{"typeContext"}, "typeContext=?",
				new String[]{entity.getTypeContext()},
				null, null, null);
		int context=cursor.getCount();
		if(context>0){
			updata(entity);
		}else{
			db.insert("trafficMessage", null, values);
		}
	}

	public void addMsg(List<TrafficMessage> entitys){
		ContentValues values = new ContentValues();
		for(TrafficMessage entity:entitys){
			values.put("typeContext", entity.getTypeContext());
			values.put("applyed", entity.getApplyed());
			values.put("surplus", entity.getSurplus());
			values.put("alled", entity.getAll());
			Cursor cursor = db.query("trafficMessage",
					new String[]{"typeContext"}, "typeContext=?",
					new String[]{entity.getTypeContext()},
					null, null, null);
			int context=cursor.getCount();
			if(context>0){
				updata(entity);
			}else{
				db.insert("trafficMessage", null, values);
			}
		}
	}

	public boolean delete(String typeContext) {
		// ???????????????
		int rownumber = db.delete("trafficMessage", "typeContext=?",
				new String[] { typeContext });
		if (rownumber == 0) {
			return false;
		} else {
			return true;
		}
	}

	public List<TrafficMessage> getTrafficMessageAll(){
		// ?????????????
		Cursor cursor = db.query("trafficMessage", new String[]{"typeContext","applyed","surplus","alled"}, null, null, null, null, null);
		List<TrafficMessage> trafficMessages = new ArrayList<TrafficMessage>();
		while(cursor.moveToNext()){
			TrafficMessage info = new TrafficMessage();
			info.setTypeContext(cursor.getString(0));
			info.setApplyed(cursor.getString(1));
			info.setSurplus(cursor.getString(2));
			info.setAll(cursor.getString(3));
			trafficMessages.add(info);
		}
		cursor.close();
		db.close();
		return trafficMessages;

	}
	public void updata(List<TrafficMessage> entitys){
		ContentValues values = new ContentValues();
		for(TrafficMessage entity:entitys){
			values.put("typeContext", entity.getTypeContext());
			values.put("applyed", entity.getApplyed());
			values.put("surplus", entity.getSurplus());
			values.put("alled", entity.getAll());
			db.update("trafficMessage", values, "typeContext=?", new String[]{entity.getTypeContext()});
		}
	}

	public boolean updata(TrafficMessage entity){
		ContentValues values = new ContentValues();
		values.put("typeContext", entity.getTypeContext());
		values.put("applyed", entity.getApplyed());
		values.put("surplus", entity.getSurplus());
		values.put("alled", entity.getAll());
		int rownumber =db.update("trafficMessage", values, "typeContext=?", new String[]{entity.getTypeContext()});
		if (rownumber == 0) {
			return false;
		} else {
			return true;
		}
	}

}
