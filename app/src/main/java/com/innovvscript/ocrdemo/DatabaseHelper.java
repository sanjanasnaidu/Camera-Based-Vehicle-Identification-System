package com.innovvscript.ocrdemo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="vehicle.db";
    public static final String TABLE_NAME="number_plates";
    public static final String COL_1="nplate";
    public static final String COL_2="name";
    public static final String COL_3="mobile";
    public static final String COL_4="email";
    public static final String COL_5="address";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table "+TABLE_NAME+"(NPLATE TEXT PRIMARY KEY,NAME TEXT,MOBILE TEXT,EMAIL TEXT,ADDRESS TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public Cursor getdata()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("select * from "+TABLE_NAME,null);
        return result;
    }

}