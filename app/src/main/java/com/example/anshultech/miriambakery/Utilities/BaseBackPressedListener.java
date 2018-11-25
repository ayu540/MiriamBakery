package com.example.anshultech.miriambakery.Utilities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.anshultech.miriambakery.Activities.BakeryHome;
import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.Fragments.BakerryRecipieDetailViewFragment;
import com.example.anshultech.miriambakery.R;

import java.util.ArrayList;

public class BaseBackPressedListener implements BakeryHome.OnBackPressedListener {
    private final FragmentActivity activity;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private int mVideosClickedPostion;
    private boolean mTwoPane = false;

    public BaseBackPressedListener(FragmentActivity activity, int VideosClickedPostion, ArrayList<BakeryStepsListBean> BakeryStepsListBeans, boolean twoPane) {
        this.activity = activity;
        this.mBakeryStepsListBeans = BakeryStepsListBeans;
        this.mVideosClickedPostion = VideosClickedPostion;
        mTwoPane = twoPane;

    }


    @Override
    public void forDetailsPageBackPressed(int currentFragmentCount) {
            BakerryRecipieDetailViewFragment bakerryRecipieDetailViewFragment = new BakerryRecipieDetailViewFragment();
            final Bundle bundle = new Bundle();
            bundle.putInt(activity.getString(R.string.clicked_position), mVideosClickedPostion);
            bundle.putParcelableArrayList(activity.getString(R.string.bakery_master_list), mBakeryStepsListBeans);
            bundle.putBoolean(activity.getString(R.string.is_two_pane), mTwoPane);
            bundle.putParcelableArrayList(activity.getString(R.string.steps_list), mBakeryStepsListBeans);
            bundle.putString(activity.getString(R.string.list_type), "Steps");
            bakerryRecipieDetailViewFragment.setArguments(bundle);
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            if (!bakerryRecipieDetailViewFragment.isAdded()) {
                fragmentTransaction
                        .replace(R.id.tabletViewFrameLayout,
                                bakerryRecipieDetailViewFragment, activity.getString(R.string.bakerryRecipieDetailViewFragment))
                        .addToBackStack(null).commit();
            } else {
                fragmentTransaction.show(bakerryRecipieDetailViewFragment);
            }
    }

}
