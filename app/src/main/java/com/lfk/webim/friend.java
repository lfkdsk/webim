package com.lfk.webim;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lfk.webim.appli.BaseActivity;
import com.lfk.webim.appli.SQLiteHelper;
import com.lfk.webim.appli.TalkLogs;
import com.lfk.webim.appli.user;
import com.lfk.webim.server.Myserver;
import com.lfk.webim.server.connect;


public class friend extends BaseActivity {
    public static ArrayAdapter<String> mArrayAdapter;
    public SwipeRefreshLayout swipeLayout;
    private SQLiteDatabase sqLiteDatabase;
    private SQLiteHelper sqLiteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        CreateNewTable();
        final ClientConServer server = new ClientConServer(this,mhandler,this.sqLiteDatabase);

        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        TextView textView=(TextView)findViewById(R.id.name);
        textView.setText(user.UserName_ + "的朋友");

        Intent intentServer= new Intent(this, Myserver.class);
        startService(intentServer);

        final ListView listView=(ListView)findViewById(R.id.friend_list);
        mArrayAdapter= new ArrayAdapter<String>(this, R.layout.list_item);
        listView.setAdapter(mArrayAdapter);

        //server.getFriends();
        //server.getChat();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String temp = (String) ((TextView) arg1).getText();
                Intent intent = new Intent();
                String temper = temp + "@lfkdsk/Smack";
                Log.e(temp + "================", temper);
                user.FromName = temper;
                user.FromName_ = temp;
                intent.putExtra("FromName", temper);
                intent.setClass(friend.this, useractivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),
                        "Chat with " + temp,
                        Toast.LENGTH_SHORT).show();
                mArrayAdapter.notifyDataSetChanged();
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {//延迟跳转=-=
                    public void run() {
                        swipeLayout.setRefreshing(true);
                        mArrayAdapter.clear();
                        server.getFriends();
                        swipeLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }

    public void CreateNewTable(){
        sqLiteHelper = new SQLiteHelper(this,"user_logs.db",null,1);    //新建.db文件
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        TalkLogs talklog = new TalkLogs(user.UserName_);                //获取新建表的语句
        sqLiteDatabase.execSQL(talklog.returnAString());                //新建表
        Toast.makeText(friend.this, user.UserName_+" Create success",Toast.LENGTH_SHORT).show();
        Log.e(user.UserName_, "success!!!");
        //sqLiteDatabase.close();
        }
    public  Handler mhandler = new Handler()
    {
        public void handleMessage(android.os.Message message)
        {
            switch (message.what) {
                case 0:
                    Bundle bundle = (Bundle)message.obj;            //桌面Toast的解决方法
                    String s1 = bundle.getString("name");
                    String s2 = bundle.getString("text");
                    showCustomToast(s1,s2);
                    break;
                case 1: {
                    String temp = (String) message.obj;
                    friend.mArrayAdapter.add(temp);
                    break;
                }
            }
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
    public void showCustomToast(String s1, String s2) {//新建显示TOAST
        // 通用的布局加载器
        LayoutInflater inflater = getLayoutInflater();
        // 加载根容器，方便用于后面的view的定位
        View layout = inflater.inflate(R.layout.toast_view, (ViewGroup)findViewById(R.id.llToast));
        // 设置图片的源文件
        ImageView image = (ImageView) layout.findViewById(R.id.tvImageToast);
        image.setImageResource(R.drawable.toast_image);
        // 设置title及内容
        TextView title = (TextView) layout.findViewById(R.id.tvTitleToast);
        title.setText(s1);
        TextView text = (TextView) layout.findViewById(R.id.tvTextToast);
        text.setText(s2);
        Toast tempToast = new Toast(getApplicationContext());
        // 设置位置
        tempToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 0);
        // 设置显示时间
        tempToast.setDuration(Toast.LENGTH_SHORT);
        tempToast.setView(layout);
        tempToast.show();
    }
}
