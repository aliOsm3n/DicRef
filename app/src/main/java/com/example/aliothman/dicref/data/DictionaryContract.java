package com.example.aliothman.dicref.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class DictionaryContract {

        public static final String TABLE_NAME = "dictionary";
        public static final String COLUMN_DIC_ID = "dic_id";
        public static final String COLUMN_CATS_TEXT = "cats";
        public static final String COLUMN_VIDEO_TEXT = "video_link";
        public static final String COLUMN_TEXTAR_TEXT = "text_ar";
        public static final String COLUMN_TEXTEN_TEXT = "text_en";
        public static final String COLUMN_SHARED_TEXT = "shared_link";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_DIC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CATS_TEXT + " TEXT,"
                    + COLUMN_VIDEO_TEXT + " TEXT,"
                    + COLUMN_TEXTAR_TEXT + " TEXT,"
                    + COLUMN_TEXTEN_TEXT + " TEXT,"
                    + COLUMN_SHARED_TEXT + " TEXT"
                    + ")";

    }


