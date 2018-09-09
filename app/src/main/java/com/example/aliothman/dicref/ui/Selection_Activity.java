package com.example.aliothman.dicref.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.aliothman.dicref.R;
import com.example.aliothman.dicref.adaptors.MyExpandableListAdapter;
import com.example.aliothman.dicref.models.Child;
import com.example.aliothman.dicref.models.Parent;
import com.example.aliothman.dicref.utils.AppUtils;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.aliothman.dicref.networking.Constants.Auth;
import static com.example.aliothman.dicref.networking.Constants.Detail_Url;

public class Selection_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<Parent> dummyList;
    ArrayList<Parent> ListWithData = new ArrayList<>() ;
    ExpandableListView listView ;
    MyExpandableListAdapter myExpandableListAdapter ;
    public int  Cat_value  ;
    private int lastExpandedPosition = -1;


    @Override
    protected void onStart() {
        super.onStart();
        // method for Set Color For Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppUtils.setStatusBarColor(Selection_Activity.this);
        }
        // Check the Connection of the Internet
        if(AppUtils.isNetworkAvailable(Selection_Activity.this) == false){
            AppUtils.showInfoToast(Selection_Activity.this,getString(R.string.ConnectionError));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar) ;

        // this method for remove name of ActionBar .
        RemoveNameActionBar();

        //initialize networking
        AndroidNetworking.initialize(getApplicationContext());

        //get value of Category from Home Activity
        Intent i = getIntent();
        Cat_value = i.getExtras().getInt("Cat_ID");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // applay Font on Navigation View Menu
        ChangeMenuFont(navigationView);


        listView = findViewById(R.id.Exlist);
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    listView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        // some methods for hide narrow and divide beyween items (group and Child)
        listView.setGroupIndicator(null);
        listView.setChildIndicator(null);
        listView.setChildDivider(getResources().getDrawable(R.color.white));
        listView.setDivider(getResources().getDrawable(R.color.gray));
        listView.setDividerHeight(8);

        FillData();
    }

    private void ChangeMenuFont(NavigationView navigationView){
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    AppUtils.applyFontToMenuItem(subMenuItem , Selection_Activity.this);
                }
            }
            //the method we have create in activity
            AppUtils.applyFontToMenuItem(mi ,Selection_Activity.this);
        }
    }

    private void RemoveNameActionBar() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // hide title
        getSupportActionBar().setDisplayShowHomeEnabled(false);   //hide  icon
    }

    public  void FillData (){
        AppUtils.showProgressDialog(this);
                AndroidNetworking.post(Detail_Url)
                .addHeaders("Authorization" ,Auth)
                .addBodyParameter("cat_id" ,String.valueOf(Cat_value))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        AppUtils.dismissProgressDialog();
                         int id = 0 ;
                         String word_ar , word_en , video_file , category_name , share_url = "" ;

                        Log.e("data" , response);
                        JSONObject item  = new JSONObject();
                       try {
                           JSONArray jsonArray = new JSONArray(response);
                           if (jsonArray.length()==0){
                               AppUtils.showInfoToast(Selection_Activity.this ,getString(R.string.no_data));
                           }
                           else if (jsonArray.length()!=0){
                           for(int i = 0 ; i<jsonArray.length() ; i++) {
                               final Parent parent = new Parent();
                               item = jsonArray.getJSONObject(i);
                               id = item.getInt("id");
                               Log.e("id", id + " ");
                               word_ar = item.getString("word_ar");
                               Log.e("word_ar", word_ar + " ");
                               word_en = item.getString("word_en");
                               Log.e("word_en", word_en + " ");
                               video_file = item.getString("video_file");
                               Log.e("video_file", video_file + " ");
                               category_name = item.getString("category_name");
                               Log.e("category_name", category_name + " ");
                               share_url = item.getString("share_url");
                               Log.e("share_url", share_url + " ");

                               parent.setId(id);
                               parent.setTextAR(word_ar);
                               parent.setTextEn(word_en);
                               parent.setText(category_name);
                               parent.setChildren(new ArrayList<Child>());
                               //Create Child class object
                               final Child child = new Child();
                               child.setVideo(video_file);
                               child.setShare(share_url);
                               parent.getChildren().add(child);
                               ListWithData.add(parent);
                           }
                           }
                       }catch (Exception e){
                           e.printStackTrace();
                           AppUtils.showInfoToast(Selection_Activity.this,getString(R.string.error));
                       }
                        myExpandableListAdapter = new MyExpandableListAdapter(ListWithData , Selection_Activity.this);
                        listView.setAdapter(myExpandableListAdapter);
                        Log.e("List_Size " , ListWithData.size()+" ");
                    }
                    @Override
                    public void onError(ANError anError) {
                     Log.e("Error" , anError.getMessage());
                        AppUtils.dismissProgressDialog();
                    }
                });
    }

//    private ArrayList<Parent> buildDummyData()
//    {
//        // Creating ArrayList of type parent class to store parent class objects
//        final ArrayList<Parent> list = new ArrayList<Parent>();
////        AndroidNetworking.post(Detail_Url)
////                .addHeaders("Authorization" ,Auth)
////                .addBodyParameter("cat_id" ,String.valueOf(Cat_value))
////                .setPriority(Priority.MEDIUM)
////                .build()
////                .getAsString(new StringRequestListener() {
////                    @Override
////                    public void onResponse(String response) {
////                         int id = 0 ;
////                         String word_ar , word_en , video_file , category_name , share_url = "" ;
////
////                        Log.e("data" , response);
////                        JSONObject item  = new JSONObject();
////                       try {
////                           JSONArray jsonArray = new JSONArray(response);
////                           for(int i = 0 ; i<jsonArray.length() ; i++){
////                               final Parent parent = new Parent();
////                               item =  jsonArray.getJSONObject(i);
////                               id = item.getInt("id");
////                               Log.e("id",id +" ");
////                               word_ar = item.getString("word_ar");
////                               Log.e("word_ar",word_ar +" ");
////                               word_en = item.getString("word_en");
////                               Log.e("word_en",word_en +" ");
////                               video_file = item.getString("video_file");
////                               Log.e("video_file",video_file +" ");
////                               category_name = item.getString("category_name");
////                               Log.e("category_name",category_name +" ");
////                               share_url = item.getString("share_url");
////                               Log.e("share_url",share_url +" ");
////                                   parent.setId(id);
////                                   parent.setTextAR(word_ar);
////                                   parent.setTextEn(word_en);
////                                   parent.setChildren(new ArrayList<Child>());
////                                   //Create Child class object
////                                   final Child child = new Child();
////                                   child.setText(category_name);
////                                   child.setVideo(String.valueOf(Uri.parse("android.resource://com.example.aliothman.expandlelist/" + R.raw.vvvv)));
////                                   child.setShare(share_url);
////                                   parent.getChildren().add(child);
////                               list.add(parent);
////                           }
////                       }catch (Exception e){
////                           e.printStackTrace();
////                       }
////                    }
////
////                    @Override
////                    public void onError(ANError anError) {
////                     Log.e("Error" , anError.getMessage());
////                    }
////                });
//
//        for (int i = 1; i < 4; i++)
//        {
//            //Create parent class object
//            final Parent parent = new Parent();
//
//            // Set values in parent class object
//            if(i==1){
//                parent.setId(1);
//                parent.setTextAR("علوة ");
//                parent.setTextEn("3elwa");
//                parent.setChildren(new ArrayList<Child>());
//
//                //Create Child class object
//                final Child child = new Child();
//                //  child.setImage(getResources().getDrawable(R.drawable.download).toString());
//                //   child.setImage(Integer.parseInt(getResources().getDrawable(R.drawable.pause).toString()));
//                child.setVideo(String.valueOf(Uri.parse("android.resource://com.example.aliothman.expandlelist/"+R.raw.vvvv)));
//                child.setText(" التواصل الاجتماعى  ");
//                child.setShare("http://ssfs.website/word/09769963");
//                parent.getChildren().add(child);
//
//            }
//            else if(i==2){
//                parent.setId(2);
//                parent.setTextAR("حمادة  ");
//                parent.setTextEn("Hamada");
//                parent.setChildren(new ArrayList<Child>());
//
//                //Create Child class object
//                final  Child child = new Child();
//                // child.setImage(getResources().getDrawable(R.drawable.pause).toString());
//                //  child.setImage(Integer.parseInt(getResources().getDrawable(R.drawable.download).toString()));
//                child.setVideo(String.valueOf(Uri.parse("android.resource://com.example.aliothman.expandlelist/"+R.raw.vvvv)));
//                child.setText(" التواصل الاجتماعى  ");
//                child.setShare("http://ssfs.website/word/09769963");
//                parent.getChildren().add(child);
//
//            }
//            else if(i==3){
//                parent.setId(3);
//                parent.setTextAR("سكلانس");
//                parent.setTextEn("Sakalans");
//                parent.setChildren(new ArrayList<Child>());
//
//                //Create Child class object
//                final  Child child = new Child();
//                // child.setImage(getResources().getDrawable(R.drawable.download).toString());
//                //  child.setImage(Integer.parseInt(getResources().getDrawable(R.drawable.pause).toString()));
//                child.setVideo(String.valueOf(Uri.parse("android.resource://com.example.aliothman.expandlelist/"+R.raw.vvvv)));
//                child.setText(" التواصل الاجتماعى  ");
//                child.setShare("http://ssfs.website/word/09769963");
//                parent.getChildren().add(child);
//
//
//            }
//
//            //Adding Parent class object to ArrayList
//            list.add(parent);
//        }
//        Log.e("List Now" ,  list.size() + "");
//        return list;
//    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        this.startActivity(new Intent(Selection_Activity.this,Home_Activity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.selection_, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        // Get the SearchView and set the searchable configuration
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String inputText = query ;
                ArrayList<Parent> SearchList ;
                SearchList =  containsString(inputText , ListWithData);
                myExpandableListAdapter = new MyExpandableListAdapter(SearchList , Selection_Activity.this);
                listView.setAdapter(myExpandableListAdapter);
                myExpandableListAdapter.notifyDataSetInvalidated();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                myExpandableListAdapter = new MyExpandableListAdapter(ListWithData , Selection_Activity.this);
                listView.setAdapter(myExpandableListAdapter);
                myExpandableListAdapter.notifyDataSetInvalidated();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private ArrayList<Parent> containsString(String testString, ArrayList<Parent> list)
    {
        Log.e("list_size" , list.size() +" ");
        ArrayList<Parent> parents2 = new ArrayList<>();
        for(int i = 0 ; i < list.size() ; i++ ){
            String result =  list.get(i).getTextEn();
            Log.e("testString" , testString);
            Log.e("result" , result);
            if (result.equalsIgnoreCase(testString)){
                Log.e("matching" , result.equals(testString) +"");
                parents2.add(list.get(i));
            }
        }
        Log.e("parents2" , parents2.size() +" ");
        return parents2 ;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Home) {
            Intent intent = new Intent(Selection_Activity.this , Home_Activity.class);
            startActivity(intent);

        } else if (id == R.id.Favourite) {
            Intent intent = new Intent(Selection_Activity.this , Favourite_Activity.class);
            startActivity(intent);

        } else if (id == R.id.About) {
            Intent intent = new Intent(Selection_Activity.this , About_Activity.class);
            startActivity(intent);

        } else if (id == R.id.Call) {
            Intent intent = new Intent(Selection_Activity.this , Contect_Activity.class);
            startActivity(intent);

        } else if (id == R.id.Policy) {
            Intent intent = new Intent(Selection_Activity.this , Policy_Activity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
