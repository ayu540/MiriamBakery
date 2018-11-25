package com.example.anshultech.miriambakery.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anshultech.miriambakery.Activities.BakeryHome;
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

public class BakeryRecipeStepsVideoPlayerFragment extends Fragment implements Player.EventListener {

    private SimpleExoPlayerView mSimpleExoPlayerView;
    private static SimpleExoPlayer mSimpleExoPlayer;
    private Context mContext;
    private String videoUrl;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private int mVideosClickedPostion;
    private TextView mRecipeVideoDescriptionTextView;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private boolean mTwoPane = false;
    private long positionPlayer;
    private boolean playWhenReady;
    private ImageView recipeStepsVideoImageView;
    private RecyclerView recipiesMasterListRecyclerView1;

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

        mVideosClickedPostion = getArguments().getInt(getResources().getString(R.string.steps_clicked_position));
        mBakeryStepsListBeans = getArguments().getParcelableArrayList(getResources().getString(R.string.video_steps_list));
        mTwoPane = getArguments().getBoolean(getResources().getString(R.string.is_two_pane));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View attachedRootView = inflater.inflate(R.layout.activity_recipe_steps_vidoes_player, container, false);

        mSimpleExoPlayerView = (SimpleExoPlayerView) attachedRootView.findViewById(R.id.recipeStepsVideoPlayerSimpleExoPlayer);
        mRecipeVideoDescriptionTextView = (TextView) attachedRootView.findViewById(R.id.recipeVideoDescriptionTextView);
        recipeStepsVideoImageView = (ImageView) attachedRootView.findViewById(R.id.recipeStepsVideoImageView);
        View view = getActivity().findViewById(R.id.recipiesMasterListRecyclerView);
        if (view instanceof RecyclerView) {
            recipiesMasterListRecyclerView1 = (RecyclerView) view;
        }

        if (savedInstanceState != null) {
            mVideosClickedPostion = savedInstanceState.getInt(getResources().getString(R.string.instance_saved_video_position));
            mBakeryStepsListBeans = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.instance_saved_video_list));
            mTwoPane = savedInstanceState.getBoolean(getResources().getString(R.string.instance_saved_two_pane));
            playWhenReady = savedInstanceState.getBoolean(getResources().getString(R.string.instance_saved_play_when_ready));
        }

        ((BakeryHome) getActivity()).setOnBackPressedListener(new BaseBackPressedListener(getActivity(), mVideosClickedPostion, mBakeryStepsListBeans, mTwoPane));

        if (mBakeryStepsListBeans.get(mVideosClickedPostion).getVideoURL() != null) {
            videoUrl = mBakeryStepsListBeans.get(mVideosClickedPostion).getVideoURL();
            if (mBakeryStepsListBeans.get(mVideosClickedPostion).getVideoURL().equalsIgnoreCase("")) {
                mSimpleExoPlayerView.setVisibility(View.GONE);
                recipeStepsVideoImageView.setVisibility(View.VISIBLE);
                if (!mBakeryStepsListBeans.get(mVideosClickedPostion).getThumbnailURL().equalsIgnoreCase("")) {
                    //Load thumbnail if present
                    Picasso.get().load((mBakeryStepsListBeans.get(mVideosClickedPostion).getThumbnailURL())).into(recipeStepsVideoImageView);
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
                    positionPlayer = savedInstanceState.getLong(getResources().getString(R.string.instance_saved_position_player));
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

        return attachedRootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getResources().getString(R.string.instance_saved_video_position), mVideosClickedPostion);
        outState.putParcelableArrayList(getResources().getString(R.string.instance_saved_video_list), mBakeryStepsListBeans);
        outState.putBoolean(getResources().getString(R.string.instance_saved_two_pane), mTwoPane);
        outState.putLong(getResources().getString(R.string.instance_saved_position_player), positionPlayer);
        outState.putBoolean(getResources().getString(R.string.instance_saved_play_when_ready), playWhenReady);
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
