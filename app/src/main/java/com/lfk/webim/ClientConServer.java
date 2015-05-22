package com.lfk.webim;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.lfk.webim.appli.user;
import com.lfk.webim.server.connect;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.packet.Message;

import java.util.Collection;


public class ClientConServer {
    private Context context;
    private Handler handlers;
    ClientConServer(Context context,Handler handler){
        this.context = context;
        this.handlers = handler;
    }

    //这里收到消息后，通过广播将消息发送到需要的地方.
    private  Handler handler = new Handler(){
        public void handleMessage(android.os.Message m) {
            Message msg = new Message();
            msg=(Message) m.obj;
            //把从服务器获得的消息通过广播发送
            String[] message = new String[]{ msg.getFrom(), msg.getBody()};
            System.out.println("==========收到消息  From==========="+ message[0]);
            System.out.println("==========收到消息  Body===========" + message[1]);
            String s = msg.getFrom();
            String s1 = s.split("@")[0];
            if(user.UserName.equals(message[0]))
                System.out.println("自己的消息就不打印了");
            {
                useractivity.mConversationArrayAdapter.add(s1 + "说：" + msg.getBody());
                android.os.Message message_send = new android.os.Message();
                Bundle bundle = new Bundle();
                bundle.putString("name",s1);
                bundle.putString("text",msg.getBody());
                message_send.obj = bundle;
                message_send.what = 0;
                handlers.sendMessage(message_send);
                //Toast.makeText(context, s2, Toast.LENGTH_SHORT).show();
            }
           // intent.putExtra("message", message);
           // context.sendBroadcast(intent);//发送广播
        }
    };

    public  void getFriends(){
            //获取用户组、成员信息。
            System.out.println("--------find start----------");
            Roster roster = connect.con.getRoster();
            Collection<RosterGroup> entriesGroup = roster.getGroups();
            System.out.println("team："+entriesGroup.size());
            for(RosterGroup group: entriesGroup){
                Collection<RosterEntry> entries = group.getEntries();
                int temp=group.getEntryCount();
                System.out.println("--------groupnumber--------" + "\n" + temp);
                System.out.println("--------groupName----------" + "\n" + group.getName());
                for (RosterEntry entry : entries) {
                    System.out.println("name："+entry.getName());
                    String string2=entry.getName();
                    android.os.Message message_list = new android.os.Message();
                    message_list.obj = string2;
                    message_list.what = 1;
                    handlers.sendMessage(message_list);
                }
            }
            System.out.println("--------find end--------");
    }
    public  void getChat(){
        //在登陆以后应该建立一个监听消息的监听器，用来监听收到的消息：
        ChatManager chatManager = connect.con.getChatManager();
        chatManager.addChatListener(new MyChatManagerListener());
    }
    /** message listener*/
     class MyChatManagerListener implements ChatManagerListener {
        public void chatCreated(Chat chat, boolean arg1) {
            chat.addMessageListener(new MessageListener(){
                @Override
                public void processMessage(Chat chat, Message msg) {
                    android.os.Message m = handler.obtainMessage();
                    m.obj=msg;
                    m.sendToTarget();
                }
            });
        }
    }
}