package com.example.aliothman.dicref.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.aliothman.dicref.R;
import com.example.aliothman.dicref.adaptors.Grid_Adapter;
import com.example.aliothman.dicref.models.Grid_Model;
import com.example.aliothman.dicref.utils.AppUtils;
import com.example.aliothman.dicref.utils.CustomTypefaceSpan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.aliothman.dicref.networking.Constants.Auth;
import static com.example.aliothman.dicref.networking.Constants.Cateogories_Url;

public class Home_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GridView gridView ;
    Grid_Adapter grid_adapter ;
    List<Grid_Model> gridModelsListt = new ArrayList<Grid_Model>();

    @Override
    protected void onStart() {
        super.onStart();
        // method for Set Color For Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppUtils.setStatusBarColor(Home_Activity.this);
        }
        // Check the Connection of the Internet
        if(AppUtils.isNetworkAvailable(Home_Activity.this) == false){
            AppUtils.showInfoToast(Home_Activity.this,getString(R.string.ConnectionError));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_home_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // this method for remove name of ActionBar .
        RemoveNameActionBar();

        //initialize networking
        AndroidNetworking.initialize(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,
                             toolbar,
                             R.string.navigation_drawer_open,
                             R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // applay Font on Navigation View Menu
           ChangeMenuFont(navigationView);


         // initiate the gridView
        gridView = findViewById(R.id.grid);
        //add data to list
        addData();
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
                    AppUtils.applyFontToMenuItem(subMenuItem , Home_Activity.this);
                }
            }
            //the method we have create in activity
            AppUtils.applyFontToMenuItem(mi ,Home_Activity.this);
        }
    }


    private void initiation() {
        grid_adapter = new Grid_Adapter(this , gridModelsListt);
        gridView.setAdapter(grid_adapter);
    }

    private void addData() {
        AppUtils.showProgressDialog(this);
        AndroidNetworking.get(Cateogories_Url)
                .addHeaders( "Authorization", Auth)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        AppUtils.dismissProgressDialog();
                        Log.e("data" , response);
                        JSONObject item = new JSONObject() ;
                        int id = 0;
                        String name , icon = "" ;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length()==0){
                                AppUtils.showInfoToast(Home_Activity.this ,getString(R.string.no_data));
                            }
                            if (jsonArray.length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    item = jsonArray.getJSONObject(i);
                                    id = item.getInt("id");
                                    name = item.getString("name");
                                    icon = item.getString("icon");
                                    gridModelsListt.add(new Grid_Model(id, name, icon));
                                }
                            }
                            initiation();

                        }catch (Exception e){
                            AppUtils.showInfoToast(Home_Activity.this,getString(R.string.error));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        AppUtils.dismissProgressDialog();
                    }
                });
    }

    private void RemoveNameActionBar() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // hide title
        getSupportActionBar().setDisplayShowHomeEnabled(false);   //hide  icon
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/GE_SS_Two_Medium.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Home) {
            // Handle the camera action
        } else if (id == R.id.Favourite) {
            Intent intent = new Intent(Home_Activity.this , Favourite_Activity.class);
            startActivity(intent);

        } else if (id == R.id.About) {
            Intent intent = new Intent(Home_Activity.this , About_Activity.class);
            startActivity(intent);

        } else if (id == R.id.Call) {
            Intent intent = new Intent(Home_Activity.this , Contect_Activity.class);
            startActivity(intent);

        } else if (id == R.id.Policy) {
            Intent intent = new Intent(Home_Activity.this , Policy_Activity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
