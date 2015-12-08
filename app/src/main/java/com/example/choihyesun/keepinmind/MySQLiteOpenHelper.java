package com.example.choihyesun.keepinmind;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by choihyesun on 15. 11. 25..
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper{
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //checkList2, checkList
        String sql = "create table todoList1 (_id integer primary key autoincrement, name text, time text, checked tinyint);";
        String sql2 = "create table storageList (_id integer primary key autoincrement, name text, time text, checked tinyint);";
        String sql3 = "create table score (_id integer primary key autoincrement, score integer);";
        db.execSQL(sql);
        db.execSQL(sql2);
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists todoList1";
        String sql2 = "drop table if exists storageList";
        String sql3 = "drop table if exists score";
        db.execSQL(sql);
        db.execSQL(sql2);
        db.execSQL(sql3);
        onCreate(db);
    }
}
