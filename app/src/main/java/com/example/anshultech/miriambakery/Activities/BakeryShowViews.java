package com.example.anshultech.miriambakery.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.anshultech.miriambakery.Bean.BakeryIngridentsListBean;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
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
            mBakeryRecipiesListBeans = getIntent().getExtras().getParcelableArrayList("BAKERY_MASTER_LIST");
            mRecipeMasterListClickedPosition = getIntent().getExtras().getInt("CLICKED_POSITION");
            mBakeryIngridentsListBeans = getIntent().getExtras().getParcelableArrayList("INGREDINET_LIST");
            mBakeryStepsListBeans = getIntent().getExtras().getParcelableArrayList("STEPS_LIST");
            mTwoPane = getIntent().getExtras().getBoolean("IS_TWO_PANE");
        }


        Bundle bundle = new Bundle();
        bundle.putInt("CLICKED_POSITION", mRecipeMasterListClickedPosition);
        bundle.putParcelableArrayList("BAKERY_MASTER_LIST", mBakeryRecipiesListBeans);
        bundle.putParcelableArrayList("INGREDINET_LIST", mBakeryIngridentsListBeans);
        bundle.putParcelableArrayList("STEPS_LIST", mBakeryStepsListBeans);
        bundle.putBoolean("IS_TWO_PANE", mTwoPane);
        BakeryIngredientsStepOptionsChoose bakeryIngredientsStepOptionsChoose = new BakeryIngredientsStepOptionsChoose();
        bakeryIngredientsStepOptionsChoose.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction
                .replace(R.id.frameLayoutPhone, bakeryIngredientsStepOptionsChoose)
                .addToBackStack(null).commit();


    }
}
