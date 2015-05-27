package com.lfk.webim.appli;

import android.util.Log;

/**
 * Created by Administrator on 2015/5/26.
 */
public class TalkLogs {
    private String ID = "_id";              //数据库主键，自增
    private String With_Id ="with_id";      //和谁聊天
    private String Logs = "talklogs";       //聊天记录
    private String If_read = "_ifread";     //是否已读
    private String dbname;                  //数据表名---为用户名，即user.UserName_
    private String CREAT_DB = "";           //数据库新建的语句

    public TalkLogs(String dbname){
        this.dbname = dbname;
        giveanameto(dbname);
        Log.e("dbname=====", this.dbname);
        Log.e("dbname参数=====",dbname);
    }
    private void giveanameto(String dbname){
        CREAT_DB = "CREATE TABLE if not exists "+dbname+"("
                +this.ID +" integer primary key autoincrement,"
                +this.With_Id+","
                +this.If_read+" integer,"
                + this.Logs+")";
    }
    public String returnAString(){
        Log.e("CREAT_DB===========",CREAT_DB);
        return CREAT_DB;
    }
}