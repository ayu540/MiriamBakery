package com.example.anshultech.miriambakery.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class BakeryRecipeStepsVideoPlayer extends AppCompatActivity {

    private SimpleExoPlayerView mSimpleExoPlayerView;
    private SimpleExoPlayer mSimpleExoPlayer;
    private Context mContext;
    private String videoUrl;
    private String longDescription;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private int mVideosClickedPostion;
    private boolean mIsFirstTimeLaunched;
    private TextView mRecipeVideoDescriptionTextView;
    private TextView mRecipieVideoNavigationTextView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_vidoes_player);
        mSimpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.recipeStepsVideoPlayerSimpleExoPlayer);
        mRecipeVideoDescriptionTextView = (TextView) findViewById(R.id.recipeVideoDescriptionTextView);
        mRecipieVideoNavigationTextView = (TextView) findViewById(R.id.recipieVideoNavigationTextView);
        mRecipieVideoNavigationTextView.setVisibility(View.GONE);
        mContext = BakeryRecipeStepsVideoPlayer.this;
        mSimpleExoPlayerView.setVisibility(View.VISIBLE);

        getIntent();
        if (getIntent() != null) {
            mVideosClickedPostion = getIntent().getExtras().getInt("STEPS_CLICKED_POSITION");
            mBakeryStepsListBeans = getIntent().getExtras().getParcelableArrayList("VIDEO_STEPS_LIST");
        }

        String videoUriToParse = new String();

        if (mBakeryStepsListBeans.get(mVideosClickedPostion).getVideoURL() != null) {
            videoUriToParse = mBakeryStepsListBeans.get(mVideosClickedPostion).getVideoURL();
            intializePlayer(Uri.parse(videoUriToParse));
        } else {
            mSimpleExoPlayerView.setVisibility(View.GONE);
        }

        mRecipeVideoDescriptionTextView.setText(mBakeryStepsListBeans.get(mVideosClickedPostion).getDescription());
    }


    private void intializePlayer(Uri videoUri) {
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        //ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

        RenderersFactory renderersFactory = new DefaultRenderersFactory(mContext);
        mSimpleExoPlayer=ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
        //mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
        mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);
        String userAgent = Util.getUserAgent(mContext, "MiriamBakery");
        MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(mContext, userAgent),
                new DefaultExtractorsFactory(), null, null);
        mSimpleExoPlayer.prepare(mediaSource);
        mSimpleExoPlayer.setPlayWhenReady(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void releasePlayer() {
        mSimpleExoPlayer.stop();
        mSimpleExoPlayer.release();
        mSimpleExoPlayer = null;
    }
}
