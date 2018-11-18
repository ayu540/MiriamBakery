package com.example.anshultech.miriambakery.Activities;


import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.anshultech.miriambakery.Adapters.BakeryDetailsRecyclerViewAdapter;
import com.example.anshultech.miriambakery.Bean.BakeryIngridentsListBean;
import com.example.anshultech.miriambakery.Bean.BakeryRecipiesListBean;
import com.example.anshultech.miriambakery.Bean.BakeryStepsListBean;
import com.example.anshultech.miriambakery.Fragments.BakeryRecipeStepsVideoPlayerFragment;
import com.example.anshultech.miriambakery.R;

import java.util.ArrayList;

public class BakerryRecipieDetailViewActivity extends AppCompatActivity {

    private ArrayList<BakeryRecipiesListBean> mBakeryRecipiesListBeans;
    private ArrayList<BakeryIngridentsListBean> mBakeryIngridentsListBeans;
    private ArrayList<BakeryStepsListBean> mBakeryStepsListBeans;
    private Context mContext;
    private int mRecipeMasterListClickedPosition;
    private BakeryDetailsRecyclerViewAdapter mbBakeryDetailsRecyclerViewAdapter;
    private RecyclerView mRecipiDetailsViewRecyClerView;
    private String RECIPE_LIST_TYPE;
    private final int BAKERY_STEPS_CLICKED = 13;
    private boolean mTwoPane = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bakery_recipie_detail_view);
        mContext = BakerryRecipieDetailViewActivity.this;
        mBakeryRecipiesListBeans = new ArrayList<BakeryRecipiesListBean>();
        mBakeryIngridentsListBeans = new ArrayList<BakeryIngridentsListBean>();
        mBakeryStepsListBeans = new ArrayList<BakeryStepsListBean>();


        mRecipiDetailsViewRecyClerView = (RecyclerView) findViewById(R.id.recipiDetailsViewRecyClerView);
        mRecipiDetailsViewRecyClerView.setLayoutManager(new LinearLayoutManager(mContext));

        if (getIntent() != null) {
            mBakeryRecipiesListBeans = getIntent().getExtras().getParcelableArrayList("BAKERY_MASTER_LIST");
            mRecipeMasterListClickedPosition = getIntent().getExtras().getInt("CLICKED_POSITION");
            RECIPE_LIST_TYPE = getIntent().getExtras().getString("LIST_TYPE");
            mTwoPane = getIntent().getExtras().getBoolean("IS_TWO_PANE");
        }

        if (savedInstanceState != null) {
            mBakeryRecipiesListBeans = savedInstanceState.getParcelableArrayList("INSTANCE_BAKERY_MASTER_LIST");
            mRecipeMasterListClickedPosition = savedInstanceState.getInt("INSTANCE_CLICKED_POSITION");
            RECIPE_LIST_TYPE = savedInstanceState.getString("INSTANCE_LIST_TYPE");
            mTwoPane = savedInstanceState.getBoolean("INSTANCE_IS_TWO_PANE");
        }
        loadRecipieListItems();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("INSTANCE_BAKERY_MASTER_LIST", mBakeryRecipiesListBeans);
        outState.putInt("INSTANCE_CLICKED_POSITION", mRecipeMasterListClickedPosition);
        outState.putString("INSTANCE_LIST_TYPE", RECIPE_LIST_TYPE);
        outState.putBoolean("INSTANCE_IS_TWO_PANE", mTwoPane);
    }

    private void loadRecipieListItems() {

        if (RECIPE_LIST_TYPE.equalsIgnoreCase("Ingredients")) {
            getSupportActionBar().setTitle("Recipie Ingredients");
            mBakeryIngridentsListBeans = getIntent().getExtras().getParcelableArrayList("INGREDINET_LIST");
            if (mBakeryIngridentsListBeans != null) {

                mbBakeryDetailsRecyclerViewAdapter = new BakeryDetailsRecyclerViewAdapter(mContext, mBakeryIngridentsListBeans
                        , new BakeryDetailsRecyclerViewAdapter.BakeryDetailsIngredientsOnClickListener() {
                    @Override
                    public void onBakeryDetailsIngredientsCliCkListenerr(int position,
                                                                         ArrayList<BakeryIngridentsListBean> bakeryIngridentsListBeans) {

                    }
                }, RECIPE_LIST_TYPE
                );

                mRecipiDetailsViewRecyClerView.setAdapter(mbBakeryDetailsRecyclerViewAdapter);
            }
        } else if (RECIPE_LIST_TYPE.equalsIgnoreCase("Steps")) {
            mBakeryStepsListBeans = getIntent().getExtras().getParcelableArrayList("STEPS_LIST");
            getSupportActionBar().setTitle("Recipie Steps");
            if (mBakeryStepsListBeans != null) {
                mbBakeryDetailsRecyclerViewAdapter = new BakeryDetailsRecyclerViewAdapter(mContext, mBakeryStepsListBeans,
                        new BakeryDetailsRecyclerViewAdapter.BakeryDetailsStepsOnClickListener() {
                            @Override
                            public void onBakeryDetailsStepsCliCkListenerr(int position,
                                                                           ArrayList<BakeryStepsListBean> bakeryStepsListBeans) {

                                Bundle bundle = new Bundle();
                                bundle.putInt("STEPS_CLICKED_POSITION", position);
                                bundle.putParcelableArrayList("VIDEO_STEPS_LIST", bakeryStepsListBeans);
                                bundle.putBoolean("IS_TWO_PANE", mTwoPane);
                                BakeryRecipeStepsVideoPlayerFragment bakeryRecipeStepsVideoPlayerFragment = new BakeryRecipeStepsVideoPlayerFragment();
                                bakeryRecipeStepsVideoPlayerFragment.setArguments(bundle);
                                //  intent.putExtras(bundle);
                                if (mTwoPane == false) {

                                    Intent intent = new Intent(mContext, BakeryRecipeStepsVideoPlayerActivity.class);
                                    intent.putExtras(bundle);
                                    startActivityForResult(intent, BAKERY_STEPS_CLICKED);

                                }
                                /*}*/

                            }
                        }, RECIPE_LIST_TYPE
                );
                mRecipiDetailsViewRecyClerView.setAdapter(mbBakeryDetailsRecyclerViewAdapter);
            }

        }
    }
}
