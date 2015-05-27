package com.lfk.webim;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lfk.webim.appli.BaseActivity;
import com.lfk.webim.appli.user;
import com.lfk.webim.server.connect;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import java.io.File;

public class useractivity extends BaseActivity {
    private ListView listView;
    public static ArrayAdapter<String> mConversationArrayAdapter;
    private ClientConServer server;
    private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.useractivity);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.in);
        TextView textView = (TextView) findViewById(R.id.username);
        textView.setText("Talk with "+user.FromName_);

        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        listView.setAdapter(mConversationArrayAdapter);

        OpenDatabase();

        Button button = (Button)findViewById(R.id.button_send);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = (EditText) findViewById(R.id.edit_text_out);
                final String content = input.getText().toString();
                String string = "ME" + ":" + content;
                android.os.Message mm = new android.os.Message();
                mm.what = 0;
                mm.obj = content;
                try {
                    XMPPConnection connection = connect.getConnection();
                    ChatManager cm = connection.getChatManager();
                    Chat chat = cm.createChat(user.FromName, new MessageListener() {
                        @Override
                        public void processMessage(Chat chat, Message msg) {
                            msg.setBody(content);
                            Log.i("---", msg.getFrom() + "说：" + msg.getBody());
                            //添加消息到聊天窗口
                        }
                    });
                    if (content.equals("")) {
                        mm.what = 1;
                        mhandle.handleMessage(mm);
                    } else {
                        mhandle.handleMessage(mm);
                        chat.sendMessage(content);
                    }
                    input.setText("");
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }

        });
    }
    public Handler mhandle = new Handler() {
        public void handleMessage(android.os.Message m) {
            switch (m.what) {
                case 1:
                    Toast.makeText(useractivity.this, "不能发送空消息", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    String respond = (String) m.obj;
                    Log.i("---", respond);
                    ContentValues values = new ContentValues();
                    values.put("with_id",user.FromName_);
                    values.put("talklogs","ME: "+respond);
                    values.put("_ifread",0);
                    Log.e("存入：", "ME: " + respond);
                    database.insert("\"" + user.UserName_ + "\"", null, values);
                    values.clear();
                    mConversationArrayAdapter.add("ME: "+respond);
                    mConversationArrayAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    //addTheListview();
                    break;
            }
        }
    };
    private void OpenDatabase(){
        String DB_NAME = "user_logs.db"; //保存的数据库文件名
        String PACKAGE_NAME = "com.lfk.webim";// 应用的包名
        String DB_PATH = "/data"
                + Environment.getDataDirectory().getAbsolutePath() +"/"
                + PACKAGE_NAME+ "/databases"; // 在手机里存放数据库的位置
        File myDataPath = new File(DB_PATH);
        String dbfile = myDataPath+"/"+DB_NAME;
        database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
        if(!checkColumnExist1(database,user.UserName_,user.FromName_)){
            Cursor cursor = database.rawQuery("Select * From "+user.UserName_+" where with_id ="+"\""+user.FromName_+"\"",null);
            if(cursor.moveToFirst()) {
                do {
                    String talklogs = cursor.getString(cursor.getColumnIndex("talklogs"));
                    mConversationArrayAdapter.add(talklogs);
                    Log.e(talklogs, "================");
                }while (cursor.moveToNext());
            }
            database.execSQL("UPDATE "+user.UserName_+" SET _ifread = 1 "+"Where with_id ="+"\""+user.FromName_+"\"");
            cursor.close();
            //database.close();
        }
    }
    public static boolean checkColumnExist1(SQLiteDatabase db, String tableName, String columnName) {
        boolean result = false ;
        Cursor cursor = null ;
        try{
            //查询一行
            cursor = db.rawQuery( "SELECT * FROM " + tableName + " LIMIT 0", null );
            result = cursor != null && cursor.getColumnIndex(columnName) != -1 ;
        }catch (Exception e){
            Log.e("","checkColumnExists1..." + e.getMessage()) ;
        }finally{
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
        }
        return result ;
    }
}