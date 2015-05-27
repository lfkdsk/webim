package com.lfk.webim.appli;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/5/25.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private Context mcontext;
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(CREAT_DB);
        Toast.makeText(mcontext, "succeed collect！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
