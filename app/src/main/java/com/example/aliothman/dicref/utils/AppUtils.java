package com.example.aliothman.dicref.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.aliothman.dicref.R;
import com.example.aliothman.dicref.ui.Home_Activity;
import com.kaopiz.kprogresshud.KProgressHUD;

public class AppUtils {
    private static KProgressHUD progressHUD;


    public static void showProgressDialog(Context context) {
        createProgress(context,null);
        progressHUD.show();
    }

    private static void createProgress(Context context,String label) {
        progressHUD = DialogUtils.createNotCancelableProgressDialog(context, label);
    }

    public static void dismissProgressDialog() {
        if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }
    }

    public static void showInfoToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void applyFontToMenuItem(MenuItem mi , Context context) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/GE_SS_Two_Medium.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public  static void setStatusBarColor(Activity context){
        //all 4 lines for set color to Status bar
        Window window = context.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(context,R.color.colorBackground));
    }



}
