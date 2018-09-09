package com.example.aliothman.dicref.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import com.example.aliothman.dicref.R;
import com.example.aliothman.dicref.adaptors.MyExpandableListAdapter;
import com.example.aliothman.dicref.adaptors.MyExpandale2Adaptor;
import com.example.aliothman.dicref.data.DictionaryDbHelper;
import com.example.aliothman.dicref.models.Child;
import com.example.aliothman.dicref.models.Parent;
import com.example.aliothman.dicref.utils.AppUtils;

import java.util.ArrayList;

public class Favourite_Activity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
        // method for Set Color For Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppUtils.setStatusBarColor(Favourite_Activity.this);
        }
    }

    ExpandableListView expandableListView ;
    MyExpandale2Adaptor expandableListAdapter ;
    ArrayList<Parent> parentArrayList  = new ArrayList<>();
    DictionaryDbHelper dictionaryDbHelper ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_);
        expandableListView = findViewById(R.id.Exlist_Fav);
        // some methods for hide narrow and divide beyween items (group and Child)
        expandableListView.setGroupIndicator(null);
        expandableListView.setChildIndicator(null);
        expandableListView.setChildDivider(getResources().getDrawable(R.color.white));
        expandableListView.setDivider(getResources().getDrawable(R.color.gray));
        expandableListView.setDividerHeight(8);

        fillData();
        expandableListAdapter = new MyExpandale2Adaptor(parentArrayList , Favourite_Activity.this);
        expandableListView.setAdapter(expandableListAdapter);
    }


    private void fillData() {
        dictionaryDbHelper = new DictionaryDbHelper(this);
        Cursor cursor = dictionaryDbHelper.retrive();
        if(cursor.moveToFirst()){
            do{
                int    id   = cursor.getInt(cursor.getColumnIndex("dic_id"));
                String cats = cursor.getString(cursor.getColumnIndex("cats"));
                String video_link = cursor.getString(cursor.getColumnIndex("video_link"));
                String text_ar = cursor.getString(cursor.getColumnIndex("text_ar"));
                String text_en = cursor.getString(cursor.getColumnIndex("text_en"));
                String shared_link = cursor.getString(cursor.getColumnIndex("shared_link"));

                Log.e("Cursor data" ,  id+ " "+ cats + " " + video_link + " " + text_ar + " " + text_en + " "+shared_link);

                final  Parent parent = new Parent();
                parent.setId(id);
                parent.setTextAR(text_ar);
                parent.setTextEn(text_en);
                parent.setText(cats);
                parent.setChildren(new ArrayList<Child>());
                final  Child child = new Child();
                child.setVideo(video_link);
                child.setShare(shared_link);
                parent.getChildren().add(child);
                parentArrayList.add(parent);
            }while (cursor.moveToNext());
        }
    }
}
