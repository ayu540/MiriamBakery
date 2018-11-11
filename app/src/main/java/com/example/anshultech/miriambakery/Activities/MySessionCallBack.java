package com.example.anshultech.miriambakery.Activities;


import android.support.v4.media.session.MediaSessionCompat;

public class MySessionCallBack extends MediaSessionCompat.Callback {

    public MySessionCallBack(){}

    @Override
    public void onPrepare() {
        super.onPrepare();
    }

    @Override
    public void onPlay() {
        super.onPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSkipToNext() {
        super.onSkipToNext();
    }

    @Override
    public void onSkipToPrevious() {
        super.onSkipToPrevious();
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