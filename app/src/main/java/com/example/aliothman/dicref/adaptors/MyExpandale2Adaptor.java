package com.example.aliothman.dicref.adaptors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.aliothman.dicref.R;
import com.example.aliothman.dicref.data.DictionaryDbHelper;
import com.example.aliothman.dicref.models.Child;
import com.example.aliothman.dicref.models.Parent;
import com.example.aliothman.dicref.ui.FullScreen_Activity;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Random;

public class MyExpandale2Adaptor extends BaseExpandableListAdapter {
    private ArrayList<Parent> parents;
    Context context ;
    private LayoutInflater inflater;
    DictionaryDbHelper dictionaryDbHelper ;
    DataSource.Factory mediaDataSourceFactory;
    Handler mainHandler;
    Uri videoURI;
    SimpleExoPlayer exoPlayer;
    SimpleExoPlayerView exoPlayerView ;

    public MyExpandale2Adaptor(ArrayList<Parent> parents, Context context) {
        this.parents = parents;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }
    //method return random color
    public int randomColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return Color.rgb(r, g, b);
    }

    // This Function used to inflate parent rows view

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parentView)
    {
        // Define object from Database
        dictionaryDbHelper = new DictionaryDbHelper(context);
        //define the position of each object or item
        final Parent parent = parents.get(groupPosition);

        // Inflate grouprow.xml file for parent rows
        convertView = inflater.inflate(R.layout.grouprow2, parentView, false);
        //set view Background Color
        View view = convertView.findViewById(R.id.myView);
        view.setBackgroundColor(randomColor());
        // Define Text Views
        ((TextView) convertView.findViewById(R.id.WordArabic)).setText(parent.getTextAR());
        ((TextView) convertView.findViewById(R.id.WordEnglish)).setText(parent.getTextEn());
        ((TextView)convertView.findViewById(R.id.Catalog_text)).setText(parent.getText());


        //initialize narrow button
        final ImageView imagenarrow = convertView.findViewById(R.id.narrow_below);
        return convertView;
    }


    // This Function used to inflate child rows view
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parentView)
    {
        final Parent parent = parents.get(groupPosition);
        final Child child = parent.getChildren().get(childPosition);

        // Inflate childrow.xml file for child rows
        convertView = inflater.inflate(R.layout.childrow, parentView, false);

        exoPlayerView = (SimpleExoPlayerView) convertView.findViewById(R.id.exo_player_view);
        showPlayer(child.getVideo());

        Button shareBTn = convertView.findViewById(R.id.Share);
        shareBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUrlLink();
            }
        });
        return convertView;
    }

    private void showPlayer(String url ) {

        try {
            mainHandler = new Handler();
            mediaDataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, null));
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            if (url.length() > 0) {

                videoURI = Uri.parse(url);

                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");

                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

                MediaSource mediaSource = new ExtractorMediaSource(videoURI,
                        dataSourceFactory,
                        extractorsFactory,
                        mainHandler,
                        null);

                exoPlayerView.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(true);

            }

        } catch (Exception e) {

            Toast.makeText(context, "exception", Toast.LENGTH_SHORT).show();

        }
    }
    public  void shareUrlLink(){
        String message = "http://ssfs.website/word/09769963";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);

        context.startActivity(Intent.createChooser(share, "Title of the dialog the system will open"));
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        //Log.i("Childs", groupPosition+"=  getChild =="+childPosition);
        return parents.get(groupPosition).getChildren().get(childPosition);
    }

    //Call when child row clicked
    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        /****** When Child row clicked then this function call *******/
        Log.e("Noise getChildId", "parent == "+groupPosition+"=  child : =="+childPosition);
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        int size=0;
        if(parents.get(groupPosition).getChildren()!=null)
            size = parents.get(groupPosition).getChildren().size();
        return size;
    }


    @Override
    public Object getGroup(int groupPosition)
    {
        Log.i("Parent getGroup", groupPosition+"=  getGroup ");

        return parents.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return parents.size();
    }

    //Call when parent row clicked
    @Override
    public long getGroupId(int groupPosition)
    {
        Log.e("Parent getGroupId", groupPosition+"");
        return groupPosition;
    }

    @Override
    public void notifyDataSetChanged()
    {
        // Refresh List rows
        super.notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty()
    {
        return ((parents == null) || parents.isEmpty());
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;

    }
}
