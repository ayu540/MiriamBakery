package com.example.anshultech.miriambakery.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anshultech.miriambakery.Activities.BakeryHome;
//import com.example.anshultech.miriambakery.Activities.MySessionCallBack;
import com.example.anshultech.miriambakery.Activities.BakeryRecipeStepsVideoPlayerActivity;
import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.R;
import com.example.anshultech.miriambakery.Utilities.BaseBackPressedListener;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BakeryRecipeStepsVideoPlayerFragment extends Fragment implements Player.EventListener{

    private SimpleExoPlayerView mSimpleExoPlayerView;
    private static SimpleExoPlayer mSimpleExoPlayer;
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
//    private DrawerLayout designNavigationViewDrawerLayout;
//    private NavigationView navigationView;
//    private Toolbar navigationDrawerToolbar;
//    private ActionBarDrawerToggle actionBarDrawerToggle;
    private boolean mTwoPane = false;
    private long positionPlayer;
    private boolean playWhenReady;
    private ImageView recipeStepsVideoImageView;
    private RecyclerView recipiesMasterListRecyclerView1;
    private RecyclerView recipiesMasterListRecyclerView;


    public BakeryRecipeStepsVideoPlayerFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBakeryStepsListBeans = new ArrayList<BakeryStepsListBean>();

        mVideosClickedPostion = getArguments().getInt("STEPS_CLICKED_POSITION");
        mBakeryStepsListBeans = getArguments().getParcelableArrayList("VIDEO_STEPS_LIST");
        mTwoPane = getArguments().getBoolean("IS_TWO_PANE");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View attachedRootView = inflater.inflate(R.layout.activity_recipe_steps_vidoes_player, container, false);



        mSimpleExoPlayerView = (SimpleExoPlayerView) attachedRootView.findViewById(R.id.recipeStepsVideoPlayerSimpleExoPlayer);
        mRecipeVideoDescriptionTextView = (TextView) attachedRootView.findViewById(R.id.recipeVideoDescriptionTextView);
        recipeStepsVideoImageView = (ImageView) attachedRootView.findViewById(R.id.recipeStepsVideoImageView);
        /*recipiesMasterListRecyclerView = (RecyclerView) attachedRootView.findViewById(R.id.recipiesMasterListRecyclerView);
        BakeryHome bakeryHome = new BakeryHome();
        bakeryHome.setmRecipiListRecyclerView(recipiesMasterListRecyclerView);
        recipiesMasterListRecyclerView1 = bakeryHome.getmRecipiListRecyclerView(); */
        View view = getActivity().findViewById(R.id.recipiesMasterListRecyclerView);
        if (view instanceof RecyclerView) {
            recipiesMasterListRecyclerView1 = (RecyclerView) view;
        }


        if (savedInstanceState != null) {
            mVideosClickedPostion = savedInstanceState.getInt("INSTANCE_SAVED_VIDEO_POSITION");
            mBakeryStepsListBeans = savedInstanceState.getParcelableArrayList("INSTANCE_SAVED_VIDEO_LIST");
            mTwoPane = savedInstanceState.getBoolean("INSTANCE_SAVED_TWO_PANE");

            playWhenReady = savedInstanceState.getBoolean("INSTANCE_SAVED_PLAY_WHEN_READY");
        }

        ((BakeryHome)getActivity()).setOnBackPressedListener(new BaseBackPressedListener(getActivity(), mVideosClickedPostion, mBakeryStepsListBeans, mTwoPane));

        if (mBakeryStepsListBeans.get(mVideosClickedPostion).getVideoURL() != null) {
            videoUrl = mBakeryStepsListBeans.get(mVideosClickedPostion).getVideoURL();
            if (mBakeryStepsListBeans.get(mVideosClickedPostion).getVideoURL().equalsIgnoreCase("")) {
                Log.d("BakeryViewActivty", "EMPTY URL");
                mSimpleExoPlayerView.setVisibility(View.GONE);
                recipeStepsVideoImageView.setVisibility(View.VISIBLE);
                if (!mBakeryStepsListBeans.get(mVideosClickedPostion).getThumbnailURL().equalsIgnoreCase("")) {
                    //Load thumbnail if present
                    Picasso.get().load((mBakeryStepsListBeans.get(mVideosClickedPostion).getThumbnailURL())).into(recipeStepsVideoImageView);
                    //Picasso.with(this).load((mBakeryStepsListBeans.get(mVideosClickedPostion).getThumbnailURL()).into(recipeStepsVideoImageView);
                } else {
                    recipeStepsVideoImageView.setVisibility(View.GONE);
                }
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mRecipeVideoDescriptionTextView.setText(mBakeryStepsListBeans.get(mVideosClickedPostion).getDescription());
                } else {
                    hideUI();
                    mSimpleExoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    mSimpleExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                }

            } else {
                if (savedInstanceState != null) {
                    //resuming by seeking to the last position
                    positionPlayer = savedInstanceState.getLong("INSTANCE_SAVED_POSITION_PLAYER");
                }
                recipeStepsVideoImageView.setVisibility(View.GONE);
                initializeMediaSession();
                intializePlayer(Uri.parse(videoUrl));
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mRecipeVideoDescriptionTextView.setText(mBakeryStepsListBeans.get(mVideosClickedPostion).getDescription());
                } else {
                    hideUI();
                    mSimpleExoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    mSimpleExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                }

            }

        } else {
            mSimpleExoPlayerView.setVisibility(View.GONE);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mRecipeVideoDescriptionTextView.setText(mBakeryStepsListBeans.get(mVideosClickedPostion).getDescription());
            } else {
                hideUI();
                mSimpleExoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                mSimpleExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            }

        }


       /* getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        BakerryRecipieDetailViewFragment bakerryRecipieDetailViewFragment = new BakerryRecipieDetailViewFragment();
                        final Bundle bundle = new Bundle();
                        bundle.putInt("CLICKED_POSITION", mVideosClickedPostion);
                        bundle.putParcelableArrayList("BAKERY_MASTER_LIST", mBakeryStepsListBeans);
                        bundle.putBoolean("IS_TWO_PANE", mTwoPane);
                        bundle.putParcelableArrayList("STEPS_LIST", mBakeryStepsListBeans);
                        bundle.putString("LIST_TYPE", "Steps");
                        bakerryRecipieDetailViewFragment.setArguments(bundle);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager
                                .beginTransaction();
                        if (!bakerryRecipieDetailViewFragment.isAdded()) {
                            fragmentTransaction
                                    .replace(R.id.tabletViewFrameLayout,
                                            bakerryRecipieDetailViewFragment, "bakerryRecipieDetailViewFragment")
                                    .addToBackStack(null).commit();
                        } else {
                            fragmentTransaction.show(bakerryRecipieDetailViewFragment);
                        }

                        return true;
                    }
                }
                return false;
            }
        });*/

       /* BakeryHome bakeryHome = new BakeryHome();
        bakeryHome.setOnBackPressedListener(new BakeryHome.OnBackPressedListener(getActivity()));

        ((BakeryHome)getActivity()).setOnBackPressedListener(new BakeryHome.OnBackPressedListener() {
            @Override
            public void onBackPressed() {
                BakerryRecipieDetailViewFragment bakerryRecipieDetailViewFragment = new BakerryRecipieDetailViewFragment();
                final Bundle bundle = new Bundle();
                bundle.putInt("CLICKED_POSITION", mVideosClickedPostion);
                bundle.putParcelableArrayList("BAKERY_MASTER_LIST", mBakeryStepsListBeans);
                bundle.putBoolean("IS_TWO_PANE", mTwoPane);
                bundle.putParcelableArrayList("STEPS_LIST", mBakeryStepsListBeans);
                bundle.putString("LIST_TYPE", "Steps");
                bakerryRecipieDetailViewFragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                if (!bakerryRecipieDetailViewFragment.isAdded()) {
                    fragmentTransaction
                            .replace(R.id.tabletViewFrameLayout,
                                    bakerryRecipieDetailViewFragment, "bakerryRecipieDetailViewFragment")
                            .addToBackStack(null).commit();
                } else {
                    fragmentTransaction.show(bakerryRecipieDetailViewFragment);
                }
            }
        });
*/

        /*getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                Fragment fragment = getFragmentManager().findFragmentByTag("bakerryRecipieDetailViewFragment");
                if (fragment instanceof BakerryRecipieDetailViewFragment) {
                    // BakerryRecipieDetailViewFragment bakerryRecipieDetailViewFragment= (BakerryRecipieDetailViewFragment) fragment;

                }
            }
        });*/


        return attachedRootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("INSTANCE_SAVED_VIDEO_POSITION", mVideosClickedPostion);
        outState.putParcelableArrayList("INSTANCE_SAVED_VIDEO_LIST", mBakeryStepsListBeans);
        outState.putBoolean("INSTANCE_SAVED_TWO_PANE", mTwoPane);
        outState.putLong("INSTANCE_SAVED_POSITION_PLAYER", positionPlayer);
        outState.putBoolean("INSTANCE_SAVED_PLAY_WHEN_READY", playWhenReady);
        Log.d("BakeryRecipiesVideoPl", "onSaveInstanceState Instance State" + outState);
    }


    private void intializePlayer(Uri videoUri) {
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();

        RenderersFactory renderersFactory = new DefaultRenderersFactory(mContext);
        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
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
        mMediaSession.setCallback(new BakeryRecipeStepsVideoPlayerActivity.MySessionCallBack());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }


    private void hideUI() {
        if ((((AppCompatActivity) getActivity()).getSupportActionBar() != null)) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            recipiesMasterListRecyclerView1.setVisibility(View.GONE);
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mSimpleExoPlayer != null) {
            positionPlayer = mSimpleExoPlayer.getContentPosition();
            playWhenReady = mSimpleExoPlayer.getPlayWhenReady();
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;

        }

    }
/*

    @Override
    public void onDetach() {
        super.onDetach();
        BakerryRecipieDetailViewFragment bakerryRecipieDetailViewFragment = new BakerryRecipieDetailViewFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt("CLICKED_POSITION", mVideosClickedPostion);
        bundle.putParcelableArrayList("BAKERY_MASTER_LIST", mBakeryStepsListBeans);
        bundle.putBoolean("IS_TWO_PANE", mTwoPane);
        bundle.putParcelableArrayList("STEPS_LIST", mBakeryStepsListBeans);
        bundle.putString("LIST_TYPE", "Steps");
        bakerryRecipieDetailViewFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        if (!bakerryRecipieDetailViewFragment.isAdded()) {
            fragmentTransaction
                    .replace(R.id.tabletViewFrameLayout,
                            bakerryRecipieDetailViewFragment, "bakerryRecipieDetailViewFragment")
                    .addToBackStack(null).commit();
        } else {
            fragmentTransaction.show(bakerryRecipieDetailViewFragment);
        }

    }
*/

    @Override
    public void onResume() {
        super.onResume();
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.setPlayWhenReady(playWhenReady);
            mSimpleExoPlayer.seekTo(positionPlayer);

        } else {
            initializeMediaSession();
            intializePlayer(Uri.parse(videoUrl));
            mSimpleExoPlayer.setPlayWhenReady(playWhenReady);
            mSimpleExoPlayer.seekTo(positionPlayer);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
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

/*
    @Override
    public void onBackPressed() {
        Log.d("here", "in vide fragment ");
    }
*/

/*    @Override
    public void doBack() {

        BakerryRecipieDetailViewFragment bakerryRecipieDetailViewFragment = new BakerryRecipieDetailViewFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt("CLICKED_POSITION", mVideosClickedPostion);
        bundle.putParcelableArrayList("BAKERY_MASTER_LIST", mBakeryStepsListBeans);
        bundle.putBoolean("IS_TWO_PANE", mTwoPane);
        bundle.putParcelableArrayList("STEPS_LIST", mBakeryStepsListBeans);
        bundle.putString("LIST_TYPE", "Steps");
        bakerryRecipieDetailViewFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        if (!bakerryRecipieDetailViewFragment.isAdded()) {
            fragmentTransaction
                    .replace(R.id.tabletViewFrameLayout,
                            bakerryRecipieDetailViewFragment, "bakerryRecipieDetailViewFragment")
                    .addToBackStack(null).commit();
        } else {
            fragmentTransaction.show(bakerryRecipieDetailViewFragment);
        }
    }*/


    public static class MySessionCallBack extends MediaSessionCompat.Callback {

        public MySessionCallBack() {
        }

        @Override
        public void onPrepare() {
            super.onPrepare();

        }

        @Override
        public void onPlay() {
            super.onPlay();
            mSimpleExoPlayer.setPlayWhenReady(true);

        }

        @Override
        public void onPause() {
            super.onPause();
            mSimpleExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            mSimpleExoPlayer.seekTo(0);
        }

        @Override
        public void onFastForward() {
            super.onFastForward();
        }

        @Override
        public void onRewind() {
            super.onRewind();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
        }
    }

}
