package com.example.aliothman.dicref.adaptors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.aliothman.dicref.R;
import com.example.aliothman.dicref.models.Grid_Model;
import com.example.aliothman.dicref.ui.Selection_Activity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid_Adapter extends BaseAdapter {
    Context context ;
    List<Grid_Model> gridModelsList = new ArrayList<>();

    public Grid_Adapter(Context context, List<Grid_Model> gridModelsList) {
        this.context = context;
        this.gridModelsList = gridModelsList;
    }

    @Override
    public int getCount() {
        return gridModelsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //inflate layout Griditem
         convertView = LayoutInflater.from(context).inflate(R.layout.grid_item , parent , false);
        final Grid_Model grid_model = gridModelsList.get(position);
        // initializa view
        View view = convertView.findViewById(R.id.myView);
        view.setBackgroundColor(randomColor());
        //initiate textview
        final TextView textView = convertView.findViewById(R.id.texttittle);
        textView.setText(grid_model.getName());
        // initiate imageview
        final ImageView imageView = convertView.findViewById(R.id.circleimageView);
        Picasso.get().load(grid_model.getPhoto()).into(imageView);
        //initiate Layout that Contain View
        final RelativeLayout relativeLayout = convertView.findViewById(R.id.Layout_relative);
        // initiate view on click
        convertView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.fill_shape_color));
                textView.setTextColor(context.getResources().getColor(R.color.white));
                imageView.setBackground(context.getResources().getDrawable(R.drawable.circle_solid));
                Intent intent = new Intent(context , Selection_Activity.class);
                intent.putExtra("Cat_ID" , grid_model.getId());
                Log.e("Cat_ID",grid_model.getId()+" ");
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    //method return random color
    public int randomColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return Color.rgb(r, g, b);
    }


}
