package com.lfk.webim.server;

        import android.app.Notification;
        import android.app.PendingIntent;
        import android.app.Service;
        import android.content.Intent;
        import android.os.IBinder;
        import android.util.Log;

        import com.lfk.webim.R;
        import com.lfk.webim.friend;

/**
 * Created by Administrator on 2015/4/12.
 */
public class Myserver extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Notification notification=new Notification(R.mipmap.ic_launcher,"Just We running",System.currentTimeMillis());
        Intent intent=new Intent(this, friend.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
        notification.setLatestEventInfo(this,"Just We","Online",pendingIntent);
        Log.d("Myservice       ","         Oncreater");
        startForeground(1, notification);
    }

}
