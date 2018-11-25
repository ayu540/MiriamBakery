package com.example.anshultech.miriambakery.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.anshultech.miriambakery.Bean.BakeryIngridentsListBean;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.Fragments.BakerryRecipieDetailViewFragment;
import com.example.anshultech.miriambakery.Fragments.BakeryIngredientsStepOptionsChooseFragment;
import com.example.anshultech.miriambakery.Fragments.BakeryRecipeStepsVideoPlayerFragment;
import com.example.anshultech.miriambakery.R;

import java.util.ArrayList;

public class BakeryShowViews extends AppCompatActivity {

    private Context mContext;
    private ArrayList<BakeryRecipiesListBean> mBakeryRecipiesListBeans;
    private ArrayList<BakeryIngridentsListBean> mBakeryIngridentsListBeans;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private int mRecipeMasterListClickedPosition;
    private final int RECIPIE_CHOOSER_LIST_CODE = 12;
    private boolean mTwoPane = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout_phone);

        mContext = BakeryShowViews.this;

        if (getIntent() != null) {
            mBakeryRecipiesListBeans = getIntent().getExtras().getParcelableArrayList(getResources().getString(R.string.bakery_master_list));
            mRecipeMasterListClickedPosition = getIntent().getExtras().getInt(getResources().getString(R.string.clicked_position));
            mBakeryIngridentsListBeans = getIntent().getExtras().getParcelableArrayList(getResources().getString(R.string.ingredient_list));
            mBakeryStepsListBeans = getIntent().getExtras().getParcelableArrayList(getResources().getString(R.string.steps_list));
            mTwoPane = getIntent().getExtras().getBoolean(getResources().getString(R.string.is_two_pane));
        }


        Bundle bundle = new Bundle();
        bundle.putInt(getResources().getString(R.string.clicked_position), mRecipeMasterListClickedPosition);
        bundle.putParcelableArrayList(getResources().getString(R.string.bakery_master_list), mBakeryRecipiesListBeans);
        bundle.putParcelableArrayList(getResources().getString(R.string.clicked_position), mBakeryIngridentsListBeans);
        bundle.putParcelableArrayList(getResources().getString(R.string.steps_list), mBakeryStepsListBeans);
        bundle.putBoolean(getResources().getString(R.string.is_two_pane), mTwoPane);
        BakeryIngredientsStepOptionsChooseFragment bakeryIngredientsStepOptionsChooseFragment = new BakeryIngredientsStepOptionsChooseFragment();
        bakeryIngredientsStepOptionsChooseFragment.setArguments(bundle);





        if (savedInstanceState != null) {

            BakeryIngredientsStepOptionsChooseFragment bakeryIngredientsStepOptionsChooseFragment1 = (BakeryIngredientsStepOptionsChooseFragment) getSupportFragmentManager().findFragmentByTag("bakeryIngredientsStepOptionsChooseFragment");
            BakerryRecipieDetailViewFragment bakerryRecipieDetailViewFragment1 = (BakerryRecipieDetailViewFragment) getSupportFragmentManager().findFragmentByTag("bakerryRecipieDetailView");
            BakeryRecipeStepsVideoPlayerFragment bakeryRecipeStepsVideoPlayerFragment1 = (BakeryRecipeStepsVideoPlayerFragment) getSupportFragmentManager().findFragmentByTag("bakeryRecipeStepsVideoPlayer");

            FragmentManager fragManager = this.getSupportFragmentManager();
            int count = this.getSupportFragmentManager().getBackStackEntryCount();
            Fragment frag = fragManager.getFragments().get(count>0?count-1:count);
            //  if (bakeryRecipeStepsVideoPlayerFragment1 != null) {

                /*String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
                getSupportFragmentManager().findFragmentByTag(tag);*/
            //   }

        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            fragmentTransaction
                    .replace(R.id.frameLayoutPhoneOptionsDetails, bakeryIngredientsStepOptionsChooseFragment, "bakeryIngredientsStepOptionsChooseFragment")
                    .addToBackStack(null).commit();
        }


    }
}