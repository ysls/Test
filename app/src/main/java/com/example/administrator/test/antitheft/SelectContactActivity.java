package com.example.administrator.test.antitheft;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.administrator.test.R;
import com.example.administrator.test.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class SelectContactActivity extends BaseActivity {

    @BindView(R.id.lv_contact)
    ListView lvContact;
    /** 联系人显示名称 **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    /** 电话号码 **/
    private static final int PHONES_NUMBER_INDEX = 1;

    private static final String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID };

    public static void startAct(Context context) {
        Intent intent = new Intent(context, SelectContactActivity.class);
        context.startActivity(intent);
    }

    public static void startActForResult(Context context) {
        Intent intent = new Intent(context, SelectContactActivity.class);
        ((Activity) context).startActivityForResult(intent, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        ButterKnife.bind(this);
        setMyTitle("选择联系人");
        initView();
    }


    @Override
    protected void initView() {

        final ArrayList<HashMap<String, String>> listContacts = readContacts();
        lvContact.setAdapter(new SimpleAdapter(this, listContacts, R.layout.item_act_select_contact, new String[]{"name", "phone"},
                new int[]{R.id.tv_name, R.id.tv_phone}));
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = listContacts.get(position);
                Intent data = new Intent();
                data.putExtra("phone", map.get("phone"));
                setResult(0, data);
                finish();
            }
        });
    }


    /**
     * 读取联系人
     */
    private ArrayList<HashMap<String, String>> readContacts() {

        ContentResolver resolver = this.getContentResolver();
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);
        ArrayList<HashMap<String, String>> listContacts = new ArrayList<>();
        String phoneNumber,phoneName;
        if(phoneCursor != null){
            while (phoneCursor.moveToNext()){

                phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX)
                        .replace(" ", "");

                if (phoneNumber==null||phoneNumber=="")
                    continue;
                phoneName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                HashMap<String, String> map = new HashMap<>();
                map.put("name",phoneName);
                map.put("phone",phoneNumber);
                listContacts.add(map);
            }
            phoneCursor.close();
        }
        return listContacts;
    }
}
