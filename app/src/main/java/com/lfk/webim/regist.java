package com.lfk.webim;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.lfk.webim.server.ConnecMethod;
import com.lfk.webim.server.connect;


public class regist extends Activity implements View.OnClickListener {
    private EditText name,password;
    //private String nametemp,pwdtemp;
    static int MSTTL=1;
    static int MSTTLS=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        name=(EditText)findViewById(R.id.login_name_reg);
        password=(EditText)findViewById(R.id.login_password_reg);
        findViewById(R.id.buttonclear_reg).setOnClickListener(this);
        findViewById(R.id.button_regist_reg).setOnClickListener(this);
        CheckBox checkBox1=(CheckBox)findViewById(R.id.show_reg);
        connect.getConnection();
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
    private android.os.Handler RegHandler = new android.os.Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    name.setText("");
                    password.setText("");
                    Log.d("+++++","=======================");
                    break;
                case 0:
                    String temp= String.valueOf(msg.obj);
                    Toast.makeText(regist.this,temp,Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    };

    private void accountRegis() {
        new Thread() {
            public void run() {
                String account = ((EditText) findViewById(R.id.login_name_reg))
                        .getText().toString();
                String pwd = ((EditText) findViewById(R.id.login_password_reg)).getText()
                        .toString();
                String is = ConnecMethod.regist(account, pwd);
                Message message=new Message();
                message.what=MSTTLS;
                message.obj=is;
                RegHandler.sendMessage(message);
            }
        }.start();
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.buttonclear_reg:
                Message message=new Message();
                message.what=MSTTL;
                RegHandler.sendMessage(message);
                break;
            case R.id.button_regist_reg:
                Log.d("======================", "Reg");
                accountRegis();
                break;
            default:
                break;
        }
    }

}
