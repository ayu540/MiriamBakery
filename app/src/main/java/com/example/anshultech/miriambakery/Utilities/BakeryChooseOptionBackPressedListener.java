package com.example.anshultech.miriambakery.Utilities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.anshultech.miriambakery.Activities.BakeryHome;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Fragments.BakeryIngredientsStepOptionsChooseFragment;
import com.example.anshultech.miriambakery.R;

import java.util.ArrayList;

public class BakeryChooseOptionBackPressedListener implements BakeryHome.OnBackOptionChoosePressedListener {
    private final FragmentActivity activity;
    private ArrayList<BakeryRecipiesListBean> mBakeryRecipiesListBeans;
    private int mRecipeMasterListClickedPosition;
    private boolean mTwoPane = false;

    public BakeryChooseOptionBackPressedListener(FragmentActivity fragmentActivity, ArrayList<BakeryRecipiesListBean> bakeryRecipiesListBeans, int recipeMasterListClickedPosition, boolean twoPane) {
        this.activity = fragmentActivity;
        mBakeryRecipiesListBeans = bakeryRecipiesListBeans;
        mRecipeMasterListClickedPosition = recipeMasterListClickedPosition;
        mTwoPane = twoPane;
    }

    @Override
    public void forOptionChooseBackPressed(int currentFragmentCount) {

        Bundle bundle = new Bundle();
        bundle.putInt("CLICKED_POSITION", mRecipeMasterListClickedPosition);
        bundle.putParcelableArrayList("BAKERY_MASTER_LIST", mBakeryRecipiesListBeans);
        bundle.putParcelableArrayList("INGREDINET_LIST", mBakeryRecipiesListBeans.get(mRecipeMasterListClickedPosition).getBakeryIngridentsListBeans());
        bundle.putParcelableArrayList("STEPS_LIST", mBakeryRecipiesListBeans.get(mRecipeMasterListClickedPosition).getBakeryStepsListBeans());
        bundle.putBoolean("IS_TWO_PANE", mTwoPane);
        BakeryIngredientsStepOptionsChooseFragment bakeryIngredientsStepOptionsChooseFragment = new BakeryIngredientsStepOptionsChooseFragment();
        bakeryIngredientsStepOptionsChooseFragment.setArguments(bundle);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        if (!bakeryIngredientsStepOptionsChooseFragment.isAdded()) {
            fragmentTransaction
                    .replace(R.id.tabletViewFrameLayout,
                            bakeryIngredientsStepOptionsChooseFragment, "bakeryIngredientsStepOptionsChooseFragment")
                    .addToBackStack(null).commit();
        } else {
            fragmentTransaction.show(bakeryIngredientsStepOptionsChooseFragment);
        }

    }
}
