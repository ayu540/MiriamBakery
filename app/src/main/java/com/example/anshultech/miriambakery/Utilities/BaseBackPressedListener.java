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
//        if (currentFragmentCount == 3) {
        /*Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag("bakerryRecipieDetailViewFragment");
        if (fragment instanceof BakerryRecipieDetailViewFragment) {*/
            BakerryRecipieDetailViewFragment bakerryRecipieDetailViewFragment = new BakerryRecipieDetailViewFragment();
            final Bundle bundle = new Bundle();
            bundle.putInt("CLICKED_POSITION", mVideosClickedPostion);
            bundle.putParcelableArrayList("BAKERY_MASTER_LIST", mBakeryStepsListBeans);
            bundle.putBoolean("IS_TWO_PANE", mTwoPane);
            bundle.putParcelableArrayList("STEPS_LIST", mBakeryStepsListBeans);
            bundle.putString("LIST_TYPE", "Steps");
            bakerryRecipieDetailViewFragment.setArguments(bundle);
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
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
//            }
     /*   } else if (count == 2) {

        } else if (count == 1) {

    }*/
        /*}*/
    }

}
