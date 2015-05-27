package com.lfk.webim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.lfk.webim.appli.user;
import com.lfk.webim.server.ConnecMethod;
import com.lfk.webim.server.connect;

public class MainActivity extends Activity implements View.OnClickListener {
    private String Tag = "MainAcy";
    private String account;
    private String pwd;
    private EditText ip;
    private EditText name;
    private EditText password;
    public CheckBox check_watch;
    public static CheckBox check_save;
    public static CheckBox check_auto;
    private SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    //private SQLiteHelper sqLiteHelper;
    //private SQLiteDatabase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=(EditText) findViewById(R.id.login_name);
        password = (EditText) findViewById(R.id.login_password);
        ip = (EditText) findViewById(R.id.login_ip);

        check_watch=(CheckBox)findViewById(R.id.show);
        check_watch.setOnCheckedChangeListener(checkBox_Listener);

        check_save=(CheckBox)findViewById(R.id.save_password);
        check_save.setOnCheckedChangeListener(checkBox_Listener);

        check_auto=(CheckBox)findViewById(R.id.auto_login_in);
        check_auto.setOnCheckedChangeListener(checkBox_Listener);

        findViewById(R.id.buttonlogin).setOnClickListener(this);
        findViewById(R.id.button_intent).setOnClickListener(this);
        findViewById(R.id.button_regist).setOnClickListener(this);

        sp = this.getSharedPreferences("userInfo",Context.MODE_PRIVATE);//存储密码
        checkbox_init();
    }
    private OnCheckedChangeListener checkBox_Listener = new OnCheckedChangeListener() {//所有checkbox的监听器
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub
                switch (buttonView.getId())
                {
                    case R.id.show:
                        if (isChecked) {
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        } else {
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                        break;
                    case R.id.save_password:
                        if (isChecked) {
                            sp.edit().putBoolean("ISCHECK", true).apply();
                        } else {
                            sp.edit().putBoolean("ISCHECK", false).apply();
                        }
                        break;
                    case R.id.auto_login_in:
                        if (isChecked) {
                            sp.edit().putBoolean("AUTO_ISCHECK", true).apply();
                        } else {
                            sp.edit().putBoolean("AUTO_ISCHECK", false).apply();
                        }
                        break;
                }
        }
    };
    private void checkbox_init() {//checkbox判断函数
        //判断记住密码多选框的状态
        if(sp.getBoolean("ISCHECK", false))
        {
            //设置默认是记录密码状态
            check_save.setChecked(true);
            ip.setText(sp.getString("USER_IP", ""));
            name.setText(sp.getString("USER_NAME",""));
            password.setText(sp.getString("PASSWORD",""));
            //判断自动登陆多选框状态
            if(sp.getBoolean("AUTO_ISCHECK", false))
            {
                //设置默认是自动登录状态
                check_auto.setChecked(true);
                //跳转界面
                //account=sp.getString("USER_NAME","");
                //pwd=sp.getString("PASSWORD","");
                Log.i("======================"+account,pwd+"===================================");
                accountLogin();
            }
        }
    }
    private void setCheck_save(){
        if(check_save.isChecked())
        {
            //记住用户名、密码、
            editor = sp.edit();
            editor.putString("USER_IP", user.My_Ip);
            editor.putString("USER_NAME", account);
            editor.putString("PASSWORD",pwd);
            editor.apply();
        }
    }
    private android.os.Handler insHandler = new android.os.Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                // 登录成功
                case 1:
                    Toast.makeText(MainActivity.this,"SUCCESS",Toast.LENGTH_SHORT).show();
                    Log.d(Tag, "login suc");
                    Intent intent=new Intent();
                    intent.putExtra("usename", account);
                    intent.setClass(MainActivity.this, friend.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                    break;
                //登录失败
                case 0:
                    Toast.makeText(MainActivity.this,"FAIL",Toast.LENGTH_SHORT).show();
                    Log.d(Tag, "login fail");
                    connect.closeConnection();
                    //accountLogin();
                default:
                    break;
            }
        }
    };

    private void accountLogin() {
        new Thread() {
            public void run() {
                user.My_Ip = ((EditText)findViewById(R.id.login_ip))
                        .getText().toString();
                account = ((EditText) findViewById(R.id.login_name))
                        .getText().toString();
                pwd = ((EditText) findViewById(R.id.login_password)).getText()
                        .toString();
                boolean is = ConnecMethod.login(account, pwd);
                if (is) {
                    insHandler.sendEmptyMessage(1);
                    // 将用户名保存
                    user.UserName = account+"@lfkdsk/Spark 2.6.3";
                    user.UserName_= account;
                    setCheck_save();
                } else {
                    insHandler.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.buttonlogin:
                accountLogin();
                break;
            case R.id.button_intent:
                Intent intent=new Intent(this,AuthorBlog.class);
                startActivity(intent);
                break;
            case R.id.button_regist:
                Intent intent1=new Intent(this,regist.class);
                startActivity(intent1);
            default:
                break;
        }
    }

}

