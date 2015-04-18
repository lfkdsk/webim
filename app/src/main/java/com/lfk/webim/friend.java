package com.lfk.webim;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lfk.webim.appli.BaseActivity;
import com.lfk.webim.appli.user;
import com.lfk.webim.server.Myserver;
import com.lfk.webim.server.connect;


public class friend extends BaseActivity {
    public static ArrayAdapter<String> mArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        Intent intent = getIntent();
        final String username = intent.getStringExtra("usename");

        TextView textView=(TextView)findViewById(R.id.name);
        textView.setText(username + "的朋友");

        Intent intentServer= new Intent(this, Myserver.class);
        startService(intentServer);

        final ListView listView=(ListView)findViewById(R.id.friend_list);
        mArrayAdapter= new ArrayAdapter<String>(this, R.layout.list_item);
        listView.setAdapter(mArrayAdapter);

        ClientConServer.findMan();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String temp= (String) ((TextView)arg1).getText();
                Intent intent = new Intent();
                String temper=temp+"@172.6.33.68/Smack";
                user.FromName=temp+"@172.6.33.68/Smack";
                user.FromName_=temp;
                intent.putExtra("FromName",temper);
                intent.setClass(friend.this, useractivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),
                        "Chat with " + temp,
                        Toast.LENGTH_SHORT).show();
                mArrayAdapter.notifyDataSetChanged();
            }

        });
    }
    public static Handler mhandler=new Handler()
    {
        public void handleMessage(android.os.Message message)
        {
            String temp=(String)message.obj;
            friend.mArrayAdapter.add(temp);
        }
    };
    protected void onDestroy()
    {
        super.onDestroy();
        connect.closeConnection();
        Intent stopintent=new Intent(this, Myserver.class);
        stopService(stopintent);
    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
