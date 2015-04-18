package com.lfk.webim;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lfk.webim.appli.BaseActivity;
import com.lfk.webim.appli.user;
import com.lfk.webim.server.connect;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class useractivity extends BaseActivity {
    private ListView listView;
    public static ArrayAdapter<String> mConversationArrayAdapter;
    private TextView text_out;
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

        Intent intent=getIntent();
        final String FromName= intent.getStringExtra("FromName");
        //connect.closeConnection();
        Button button=(Button)findViewById(R.id.button_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input=(EditText) findViewById(R.id.edit_text_out);
                final String content=input.getText().toString();
                String string= "ME"+":"+content;
                android.os.Message mm=new android.os.Message();
                mm.obj=string;
                mhandle.handleMessage(mm);
                try {
                    XMPPConnection connection = connect.getConnection();
                    ChatManager cm = connection.getChatManager();
                    Chat chat=cm.createChat(FromName, new MessageListener() {
                        @Override
                        public void processMessage(Chat chat, Message msg) {
                            msg.setBody(content);
                            Log.i("---", msg.getFrom() + "说：" + msg.getBody());
                            //添加消息到聊天窗口  ,
                        }
                    });
                    Message m = new Message();
                    m.setBody(content);
                    chat.sendMessage(m.getBody());
                    input.setText("");
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
            Handler mhandle= new Handler()
            {
                public void handleMessage(android.os.Message m) {
                    text_out=(TextView)findViewById(R.id.text_out);
                    String respond=(String)m.obj;
                    Log.i("---",respond);
                    mConversationArrayAdapter.add(respond);
                }
            };
        });

    }

}