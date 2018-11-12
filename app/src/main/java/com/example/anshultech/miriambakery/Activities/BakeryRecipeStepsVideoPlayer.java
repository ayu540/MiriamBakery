package com.example.anshultech.miriambakery.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class BakeryRecipeStepsVideoPlayer extends Fragment implements Player.EventListener, BakeryHome.OnBackPressedListener {

    private SimpleExoPlayerView mSimpleExoPlayerView;
    private SimpleExoPlayer mSimpleExoPlayer;
    private Context mContext;
    private String videoUrl;
    private String longDescription;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private int mVideosClickedPostion;
    private boolean mIsFirstTimeLaunched;
    private TextView mRecipeVideoDescriptionTextView;
    //   private TextView mRecipieVideoNavigationTextView;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private boolean doubleBackToExitPressedOnce = false;

    //navigation drawer layout
    private DrawerLayout designNavigationViewDrawerLayout;
    private NavigationView navigationView;
    private Toolbar navigationDrawerToolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private boolean mTwoPane = false;

    public BakeryRecipeStepsVideoPlayer() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View attachedRootView = inflater.inflate(R.layout.activity_recipe_steps_vidoes_player, container, false);

        mSimpleExoPlayerView = (SimpleExoPlayerView) attachedRootView.findViewById(R.id.recipeStepsVideoPlayerSimpleExoPlayer);
        mRecipeVideoDescriptionTextView = (TextView) attachedRootView.findViewById(R.id.recipeVideoDescriptionTextView);

        mBakeryStepsListBeans = new ArrayList<BakeryStepsListBean>();

        mVideosClickedPostion = getArguments().getInt("STEPS_CLICKED_POSITION");
        mBakeryStepsListBeans = getArguments().getParcelableArrayList("VIDEO_STEPS_LIST");
        mTwoPane = getArguments().getBoolean("IS_TWO_PANE");

        if (savedInstanceState != null) {
            mVideosClickedPostion = savedInstanceState.getInt("INSTANCE_SAVED_VIDEO_POSITION");
            mBakeryStepsListBeans = savedInstanceState.getParcelableArrayList("INSTANCE_SAVED_VIDEO_LIST");
        }


        mSimpleExoPlayerView.setVisibility(View.VISIBLE);

        if (mBakeryStepsListBeans.get(mVideosClickedPostion).getVideoURL() != null) {
            String videoUriToParse = mBakeryStepsListBeans.get(mVideosClickedPostion).getVideoURL();
            intializePlayer(Uri.parse(videoUriToParse));
            initializeMediaSession();
        } else {
            mSimpleExoPlayerView.setVisibility(View.GONE);
        }

        mRecipeVideoDescriptionTextView.setText(mBakeryStepsListBeans.get(mVideosClickedPostion).getDescription());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mBakeryStepsListBeans.get(mVideosClickedPostion).getShortDescription());
        //navigation Drawer Layout


        //   ((AppCompatActivity) getActivity()).setSupportActionBar(navigationDrawerToolbar);

        //ActionBar actionBar = getSupportActionBar();
        if (mTwoPane == false) {
            designNavigationViewDrawerLayout = attachedRootView.findViewById(R.id.design_navigation_view);
            navigationView = attachedRootView.findViewById(R.id.navigationView);
            navigationDrawerToolbar = attachedRootView.findViewById(R.id.navigationDrawerToolbar);
            loadNavigationDrawer();
        }


        return attachedRootView;
    }

    private void loadNavigationDrawer() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), designNavigationViewDrawerLayout, navigationDrawerToolbar,
                R.string.drawer_open, R.string.drawer_close);
        designNavigationViewDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_launcher_background);


        //add menu items dynamically
        Menu menu = navigationView.getMenu();
        if (menu.size() == 0) {
            for (int i = 0; i < mBakeryStepsListBeans.size(); i++) {
                menu.add(mBakeryStepsListBeans.get(i).getShortDescription());
            }
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                for (int i = 0; i < navigationView.getMenu().size(); i++) {
                    if (menuItem == navigationView.getMenu().getItem(i)) {
                        BakeryRecipeStepsVideoPlayer bakeryRecipeStepsVideoPlayer = new BakeryRecipeStepsVideoPlayer();
                        Bundle bundle = new Bundle();
                        bundle.putInt("STEPS_CLICKED_POSITION", i);
                        bundle.putParcelableArrayList("VIDEO_STEPS_LIST", mBakeryStepsListBeans);
                        bundle.putBoolean("IS_TWO_PANE", mTwoPane);
                        bakeryRecipeStepsVideoPlayer.setArguments(bundle);
                        releasePlayer();
                    }
                }
                menuItem.setChecked(true);
                designNavigationViewDrawerLayout.closeDrawers();
                return true;
            }
        });
    }


//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putInt("INSTANCE_SAVED_VIDEO_POSITION", mVideosClickedPostion);
//        outState.putParcelableArrayList("INSTANCE_SAVED_VIDEO_LIST", mBakeryStepsListBeans);
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        mVideosClickedPostion = savedInstanceState.getInt("INSTANCE_SAVED_VIDEO_POSITION");
//        mBakeryStepsListBeans = savedInstanceState.getParcelableArrayList("INSTANCE_SAVED_VIDEO_LIST");
//    }

    private void intializePlayer(Uri videoUri) {
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        //ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

        RenderersFactory renderersFactory = new DefaultRenderersFactory(mContext);
        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
        //mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
        mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);
        String userAgent = Util.getUserAgent(mContext, "MiriamBakery");
        MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(mContext, userAgent),
                new DefaultExtractorsFactory(), null, null);
        mSimpleExoPlayer.prepare(mediaSource);
        mSimpleExoPlayer.setPlayWhenReady(true);
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(mContext, "BakeryRecipiesVideoActivity");

        // Enable callbacks from MediaButtons and TransportControls.

        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallBack());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }


    @Override
    public void doBack() {

        if (mTwoPane == false) {
            if (designNavigationViewDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                this.doubleBackToExitPressedOnce = false;
                designNavigationViewDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }
        getFragmentManager().popBackStack("CHOOSE_OPTION_LAYOUT", FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }
 /*   @Override
    public void onBackPressed() {
        //   super.onBackPressed();



//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);


    }*/

    @Override
    public void onPause() {
        super.onPause();
 /*       if (mSimpleExoPlayer != null) {
            releasePlayer();
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
   /*     if (mSimpleExoPlayer != null) {
            releasePlayer();
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // switch (item.getItemId()) {
//            case R.id.navMenuHomePage: {
        designNavigationViewDrawerLayout.openDrawer(GravityCompat.START);
        NavUtils.navigateUpFromSameTask(getActivity());
        //   return true;
//            }
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void releasePlayer() {
        mSimpleExoPlayer.stop();
        mSimpleExoPlayer.release();
        mSimpleExoPlayer = null;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if ((Player.STATE_READY == playbackState) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mSimpleExoPlayer.getCurrentPosition(), 1f);

        } else if ((Player.STATE_READY == playbackState) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mSimpleExoPlayer.getCurrentPosition(), 1f);
        }

        mMediaSession.setPlaybackState(mStateBuilder.build());

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        actionBarDrawerToggle.syncState();
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }


}
