package com.example.storgepro;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_Sqlite extends SQLiteOpenHelper {
    public static final String DBname = "mdata.db";

    public DB_Sqlite(Context context) {
        super(context,DBname,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table mytable (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, savedata TEXT, email TEXT,phone TEXT)");
        ContentValues contentValues = new ContentValues();
        contentValues.put("username","null");
        contentValues.put("password","null");
        contentValues.put("savedata","0");
        contentValues.put("email","null");
        contentValues.put("phone","null");
        sqLiteDatabase.insert("mytable",null,contentValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS mytable");
        onCreate(sqLiteDatabase);
    }
    public String get_check(){
        String check;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select savedata from mytable",null);
        res.moveToFirst();
        check = res.getString(res.getColumnIndex("savedata"));
        return check;
    }

    public String get_username(){
        String username;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select username from mytable",null);
        res.moveToFirst();
        username = res.getString(res.getColumnIndex("username"));
        return username;
    }
    public String get_password(){
        String password;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select password from mytable",null);
        res.moveToFirst();
        password = res.getString(res.getColumnIndex("password"));
        return password;
    }
    public String get_email(){
        String password;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select email from mytable",null);
        res.moveToFirst();
        password = res.getString(res.getColumnIndex("email"));
        return password;
    }
    public boolean updateData_R(String name,String password,String check_save_data,String email,String phone)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",name);
        contentValues.put("password",password);
        contentValues.put("savedata",check_save_data);
        contentValues.put("email",email);
        contentValues.put("phone",phone);
        db.update("mytable",contentValues,"id=?",new String[]{"1"});
        return true;
    }
    public boolean updateData_Login(String name,String password,String check_save_data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",name);
        contentValues.put("password",password);
        contentValues.put("savedata",check_save_data);
        db.update("mytable",contentValues,"id=?",new String[]{"1"});
        return true;
    }
    public boolean updateData_save(String check_save_data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("savedata",check_save_data);
        db.update("mytable",contentValues,"id=?",new String[]{"1"});
        return true;
    }
}
