package com.lfk.webim.appli;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.lfk.webim.MainActivity;
import com.lfk.webim.R;
import com.lfk.webim.server.Myserver;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings_base:
                MainActivity.check_save.setChecked(false);
                MainActivity.check_auto.setChecked(false);
                MainActivity.editor.putString("USER_NAME","");
                MainActivity.editor.putString("PASSWORD", "");
                user.UserName = " ";
                user.UserName_ = " ";
                user.FromName_ = " ";
                user.FromName = " ";
                user.My_Ip = " ";
                Log.e(user.UserName_,user.UserName+"delete success");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                Intent stopintent=new Intent(this, Myserver.class);
                this.finish();
                stopService(stopintent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
