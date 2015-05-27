package com.lfk.webim;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ClientConServer {
    private Context context;
    private Handler handlers;
    private SQLiteDatabase sqLiteDatabase;

    ClientConServer(Context context,Handler handler,SQLiteDatabase sqLiteDatabase){
        this.context = context;
        this.handlers = handler;
        this.sqLiteDatabase = sqLiteDatabase;
        getFriends();
        getChat();
        System.out.print(isAppForground(context));
    }
    //这里收到消息
    private  Handler handler = new Handler(){
        public void handleMessage(android.os.Message m) {
            Message msg = new Message();
            msg = (Message) m.obj;
            //把从服务器获得的消息通过发送
            String[] message = new String[]{ msg.getFrom(), msg.getBody()};
            System.out.println("==========收到消息  From==========="+ message[0]);
            System.out.println("==========收到消息  Body===========" + message[1]);
            String s = msg.getFrom();
            String s1 = s.split("@")[0];
            ContentValues values = new ContentValues();
            if(message[1]!=null) {
                if (user.UserName.equals(message[0])) {
                    values.put("with_id",s1);
                    values.put("talklogs", "ME: " + msg.getBody());
                    values.put("_ifread",0);
                    Log.e("存入：", "ME: " + msg.getBody());
                    sqLiteDatabase.insert("\"" + user.UserName_ + "\"", null, values);
                    values.clear();
                } else {
                    values.put("with_id", s1);
                    values.put("talklogs", s1 + "说：" + msg.getBody());
                    values.put("_ifread", 0);
                    Log.e("存入：", s1 + "说：" + msg.getBody());
                    sqLiteDatabase.insert("\"" + user.UserName_ + "\"", null, values);
                    addUnread(values,s1);
                }
                if(isHome(context)){
                    sendToast(msg,s1);
                }
            }
        }
    };
    public void getFriends(){
            //获取用户组、成员信息
            System.out.println("--------find start----------");
            Roster roster = connect.con.getRoster();
            Collection<RosterGroup> entriesGroup = roster.getGroups();
            System.out.println("team：" + entriesGroup.size());
            for(RosterGroup group: entriesGroup){
                Collection<RosterEntry> entries = group.getEntries();
                int temp=group.getEntryCount();
                System.out.println("--------groupnumber--------" + "\n" + temp);
                System.out.println("--------groupName----------" + "\n" + group.getName());
                for (RosterEntry entry : entries) {
                    System.out.println("name："+entry.getName());
                    String string2 = entry.getName();
                    android.os.Message message_list = new android.os.Message();
                    message_list.obj = string2;
                    message_list.what = 1;
                    handlers.sendMessage(message_list);
                }
            }
            System.out.println("--------find end--------");
    }
    private void addUnread(ContentValues values,String s1){
        if (isAppForground(context)&& s1.equals(user.FromName_)) {
            Cursor cursor = sqLiteDatabase.rawQuery("Select * From "+user.UserName_+" where with_id ="+"\""+user.FromName_+"\""+"And _ifread ="+0,null);
            if(cursor.moveToFirst()) {
                do {
                    String talklogs = cursor.getString(cursor.getColumnIndex("talklogs"));
                    useractivity.mConversationArrayAdapter.add(talklogs);
                    Log.e(talklogs, "================");
                }while (cursor.moveToNext());
            }
            if(!isAppForground(context))
                useractivity.mConversationArrayAdapter.notifyDataSetChanged();
            cursor.close();
        }
        values.clear();
    }
        private void sendToast(Message msg,String s1){
            android.os.Message message_send = new android.os.Message();
            Bundle bundle = new Bundle();
            bundle.putString("name",s1);
            bundle.putString("text",msg.getBody());
            message_send.obj = bundle;
            message_send.what = 0;
            handlers.sendMessage(message_send);
        }
    private void getChat(){
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
                    m.obj = msg;
                    m.sendToTarget();
                }
            });
        }
    }
    public boolean isAppForground(Context mContext) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
                return false;
            }
        }
        return true;
    }
    public boolean isHome(Context mContext){
        ActivityManager mActivityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes(mContext).contains(rti.get(0).topActivity.getPackageName());
        }
    /**
     * 获得属于桌面的应用的应用包名称
     * @return 返回包含所有包名的字符串列表
     */
    private List<String> getHomes(Context mContext) {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = mContext.getPackageManager();
        //属性
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo ri : resolveInfo){
            names.add(ri.activityInfo.packageName);
            System.out.println(ri.activityInfo.packageName);
        }
        return names;
    }
}