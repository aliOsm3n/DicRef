package com.example.aliothman.dicref.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.aliothman.dicref.R;
import com.example.aliothman.dicref.utils.AppUtils;

import org.json.JSONObject;

import static com.example.aliothman.dicref.networking.Constants.Auth;
import static com.example.aliothman.dicref.networking.Constants.Static_Content_Url;

public class Policy_Activity extends AppCompatActivity {
    TextView  textPolicy ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_);
        textPolicy = findViewById(R.id.policyText);
        FillWithData();
    }

    private void FillWithData() {
        AppUtils.showProgressDialog(this);
        AndroidNetworking.post(Static_Content_Url)
                .addHeaders("Authorization" ,Auth)
                .addBodyParameter("content_slug" ,"usage-policy")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        AppUtils.dismissProgressDialog();
                        String Data ;
                        Log.e("data" , response);

                        try {
                            JSONObject item  = new JSONObject(response);
                            if (item.length()==0){
                                AppUtils.showInfoToast(Policy_Activity.this ,getString(R.string.no_data));
                            }
                            else if (item.length()!=0){

                                Data = item.getString("content");
                                textPolicy.setText(Data);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            AppUtils.showInfoToast(Policy_Activity.this,getString(R.string.error));
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.e("Error" , anError.getMessage());
                        AppUtils.dismissProgressDialog();
                    }
                });
    }
}
