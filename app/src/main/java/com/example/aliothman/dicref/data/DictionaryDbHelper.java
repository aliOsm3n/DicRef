package com.example.aliothman.dicref.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import static com.example.aliothman.dicref.data.DictionaryContract.COLUMN_CATS_TEXT;
import static com.example.aliothman.dicref.data.DictionaryContract.COLUMN_DIC_ID;
import static com.example.aliothman.dicref.data.DictionaryContract.COLUMN_SHARED_TEXT;
import static com.example.aliothman.dicref.data.DictionaryContract.COLUMN_TEXTAR_TEXT;
import static com.example.aliothman.dicref.data.DictionaryContract.COLUMN_TEXTEN_TEXT;
import static com.example.aliothman.dicref.data.DictionaryContract.COLUMN_VIDEO_TEXT;
import static com.example.aliothman.dicref.data.DictionaryContract.TABLE_NAME;

public class DictionaryDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "dictionary.db";
    SQLiteDatabase sqLiteDatabase;
    Context context;

    public DictionaryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // create dictionary table
        db.execSQL(DictionaryContract.CREATE_TABLE);
        Toast.makeText(context,"oncreate", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);

    }

    public boolean insert(int dic_id, String cats , String video_link, String text_ar , String text_en , String shared_link) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DIC_ID, dic_id);
        contentValues.put(COLUMN_CATS_TEXT, cats);
        contentValues.put(COLUMN_VIDEO_TEXT, video_link);
        contentValues.put(COLUMN_TEXTAR_TEXT, text_ar);
        contentValues.put(COLUMN_TEXTEN_TEXT, text_en);
        contentValues.put(COLUMN_SHARED_TEXT, shared_link);
        long num = db.insert(TABLE_NAME, null, contentValues);
        Log.e("num",num  +" ");

        if (num == -1) {
            Toast.makeText(context , "Insert failed", Toast.LENGTH_SHORT).show();
            Log.e("insert ", "Insert failed");
            return false;
        } else {
            Toast.makeText(context , "Insert Success", Toast.LENGTH_SHORT).show();
            Log.e("insert ", "Insert Success");
            return true;
        }
    }


    public Cursor retrive() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME  ,null);
        return cursor;
    }


    public int delete(int flag){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_DIC_ID+" = ?",new String[]{String.valueOf(flag)});
    }


}
